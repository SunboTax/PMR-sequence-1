package ec.pmr.sequence1.data

import android.app.Application
import android.util.Log
import androidx.room.Room
import ec.pmr.sequence1.data.api.*
import ec.pmr.sequence1.data.api.model.TodoItem
import ec.pmr.sequence1.data.api.model.TodoList
import ec.pmr.sequence1.data.api.response.*
import ec.pmr.sequence1.data.db.TodoDatabase
import ec.pmr.sequence1.data.db.model.Itemdb
import ec.pmr.sequence1.data.db.model.Listdb
import ec.pmr.sequence1.data.db.model.Userdb
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.Integer.min


class DataProvider(baseUrl: String,app:Application) {

    private val CAT = "DataProvider"
    private var listID = 0
    private var itemID = 0

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val todoService = retrofit.create<TodoService>()

    private val database = Room.databaseBuilder(
        app,
        TodoDatabase::class.java, "todo-database"
    ).build()

    private val userDao = database.userDao()
    private val listDao = database.listDao()
    private val itemDao = database.itemDao()

    suspend fun getUser(username: String, password: String): UserResponse {
        return try {
            Log.d(CAT, "getting user info")
            val userResponse = todoService.login(username, password)
            userDao.saveOrUpdate(Userdb(username, password, userResponse.hash, true))
            userResponse
        } catch (exception: Exception) {
            val userdb = userDao.getUser(username, password)
            return if (userdb == null) {
                UserResponse(false, "")
            } else {
                UserResponse(true, userdb.token)
            }

        }
    }

    suspend fun addUser(username: String, password: String, token: String): UserResponse {
        val userResponse = todoService.addUser(username, password, token)
        Log.d("MainActivity", "${userResponse.toString()}")
        return if (userResponse.success) {
            userResponse
        } else {
            UserResponse(false, token)
        }
    }

    suspend fun getAllLists(username: String, token: String): ListsResponse {
        return try {
            val listsResponse = todoService.getLists(token)
            val list = listsResponse.listsResponse2db(username)
            for (it in list) {
                listDao.saveOrUpdate(it)
                listID = min(listID, it.id - 1)
            }
            listsResponse
        } catch (exception: Exception) {
            val lists = listDao.getAllLists(username)
            for (list in lists) {
                listID = min(listID, list.id - 1)
            }
            lists.listsDb2Response()
        }
    }

    private fun ListsResponse.listsResponse2db(username: String): List<Listdb> {
        val list = mutableListOf<Listdb>()
        for (it in this.lists) {
            list.add(Listdb(it.id, it.label, username, true))
        }
        return list
    }

    private fun List<Listdb>.listsDb2Response(): ListsResponse {
        val list = mutableListOf<TodoList>()
        for (it in this) {
            list.add(TodoList(it.id, it.label))
        }
        return ListsResponse(true, list as List<TodoList>)
    }

    suspend fun addList(username: String, label: String, token: String): ListResponse {
        return try {
            val listResponse = todoService.addList(label, token)
            listDao.saveOrUpdate(
                Listdb(
                    listResponse.list.id,
                    listResponse.list.label,
                    username,
                    true
                )
            )
            listResponse
        } catch (exception: Exception) {
            userDao.updateUserState(username,false)
            val listdb = Listdb(this.listID, label, username, false)
            this.listID -= 1
            listDao.saveOrUpdate(listdb)
            ListResponse(true, TodoList(listdb.id, listdb.label))
        }
    }

    suspend fun deleteList(listId: Int, label: String, token: String) {
        try {
            todoService.deleteList(listId, token)
            listDao.deleteList(label)
        } catch (exception: Exception) {
            listDao.deleteList(label)
        }
    }

    suspend fun getAllItems(token: String, listId: Int): ItemsResponse {
        return try {
            val itemsResponse = todoService.getItems(listId, token)
            val list = itemsResponse.itemsResponse2db(listId)
            for (it in list) {
                itemDao.saveOrUpdate(it)
                itemID = min(itemID, it.id - 1)
            }
            itemsResponse
        } catch (exception: Exception) {

            val items = itemDao.getAllItems(listId)
            Log.d("ShowActivity","${items.toString()}")
            for (item in items) {
                itemID = min(itemID, item.id - 1)
            }
            Log.d("ShowActivity","${items.toString()}")
            items.itemsDb2Response()
        }
    }

    private fun ItemsResponse.itemsResponse2db(listId: Int): List<Itemdb> {
        val list = mutableListOf<Itemdb>()
        for (it in this.items) {
            list.add(Itemdb(it.id, listId, it.label, it.checked, true))
        }
        return list
    }

    private fun List<Itemdb>.itemsDb2Response(): ItemsResponse {
        val list = mutableListOf<TodoItem>()
        for (it in this) {
            list.add(TodoItem(it.id, it.label, it.checked))
        }
        return ItemsResponse(true, list as List<TodoItem>)
    }

    suspend fun addItem(listId: Int, token: String, label: String): ItemResponse {
        return try {
            val itemResponse = todoService.addItem(listId, label, token)
            itemDao.saveOrUpdate(
                Itemdb(
                    itemResponse.item.id,
                    listId,
                    itemResponse.item.label,
                    itemResponse.item.checked,
                    true
                )
            )
            itemResponse
        } catch (exception: Exception) {
            val itemdb = Itemdb(this.itemID, listId, label, "0", false)
            this.itemID -= 1
            itemDao.saveOrUpdate(itemdb)
            userDao.updateUserState(listDao.getList(listId).username,false)
            listDao.updateListState(listId,false)
            ItemResponse(true, TodoItem(itemdb.id, itemdb.label, itemdb.checked))
        }
    }

    suspend fun changeItem(
        listId: Int,
        itemId: Int,
        isChecked: String,
        token: String
    ): ItemResponse {
        return try {
            val itemResponse = todoService.changeItem(listId, itemId, isChecked, token)
            itemDao.changeItemState(itemId, isChecked)
            itemResponse
        } catch (exception: Exception) {
            userDao.updateUserState(listDao.getList(listId).username,false)
            listDao.updateListState(listId,false)
            itemDao.changeItemUpdateState(itemId, false)
            itemDao.changeItemState(itemId, isChecked)
            val itemdb = itemDao.getItem(itemId)
            ItemResponse(true, TodoItem(itemId, itemdb.label, itemdb.checked))
        }
    }

    suspend fun deleteItem(listId: Int, itemId: Int, token: String) {
        try {
            todoService.deleteItem(listId, itemId, token)
            itemDao.deleteItem(itemId)
        } catch (exception: Exception) {
            userDao.updateUserState(listDao.getList(listId).username,false)
            listDao.updateListState(listId,false)
            itemDao.deleteItem(itemId)
        }
    }

    suspend fun uploadAllChange() {

        val users = userDao.getAllUsers()
        for (userLocal in users) {

            val setLocal = mutableSetOf<Int>()

            // if user is not updated in the server, then update the user info
            if (!userLocal.updated) {
                Log.d("MainActivity", userLocal.toString())
                val userServer = todoService.login(userLocal.username, userLocal.password)
                // update all lists
                var listsLocal = listDao.getAllLists(userLocal.username)
                val listsServer = todoService.getLists(userServer.hash)
                Log.d("MainActivity", listsLocal.toString())
                Log.d("MainActivity", "")
                Log.d("MainActivity", listsServer.toString())
                for (list in listsLocal) {

                    setLocal.add(list.id)
                    val setLocal1 = mutableSetOf<Int>()

                    if (!list.updated) {

                        if (list.id <= 0) {
                            //update the new lists to the server and update the local list id
                            val listResponse = todoService.addList(list.label, userServer.hash)
                            Log.d("MainActivity", "newList:${listResponse.toString()}")
                            val items = itemDao.getAllItems(list.id)
                            for (item in items) {
                                itemDao.changeItemListId(item.id, listResponse.list.id)
                                // create new items in this list
                                val itemResponse = todoService.addItem(
                                    listResponse.list.id,
                                    item.label,
                                    userServer.hash
                                )
                                itemDao.updateItemId(
                                    itemResponse.item.id,
                                    itemResponse.item.label,
                                    list.id
                                )
                                if (item.checked == "1") {
                                    todoService.changeItem(
                                        listResponse.list.id,
                                        itemResponse.item.id,
                                        "0",
                                        userServer.hash
                                    )
                                }
                            }
                            listDao.updateListId(listResponse.list.id, listResponse.list.label)
                            listDao.updateListState(list.id, true)
                            Log.d("MainActivity", "newList:${listDao.getAllLists()}")
                        } else {
                            val itemLocal = itemDao.getAllItems(list.id)
                            Log.d("MainActivity","itemLocal:\n${itemLocal.toString()}")
                            for (item in itemLocal) {
                                setLocal1.add(item.id)

                                if (!item.updated) {
                                    Log.d("MainActivity","item:${item.toString()}")
                                    if (item.id <= 0) {
                                        val itemResponse = todoService.addItem(
                                            item.listId,
                                            item.label,
                                            userServer.hash
                                        )
                                        Log.d("MainActivity","${userServer.hash}")
                                        Log.d("MainActivity","${itemResponse.toString()}")
                                        itemDao.updateItemId(
                                            itemResponse.item.id,
                                            itemResponse.item.label,
                                            list.id
                                        )
                                    } else {
                                        val itemResponse = todoService.getItem(
                                            item.listId,
                                            item.id,
                                            userServer.hash
                                        )
                                        if (itemResponse.item.checked != item.checked) {
                                            todoService.changeItem(
                                                item.listId,
                                                item.id,
                                                item.checked,
                                                userServer.hash
                                            )
                                        }
                                    }
                                    itemDao.changeItemUpdateState(item.id, true)
                                }
                            }

                            val itemServer = todoService.getItems(list.id, userServer.hash)
                            for (item in itemServer.items) {
                                if (item.id !in setLocal1) {
                                    todoService.deleteItem(list.id, item.id, userServer.hash)
                                }
                            }
                        }
                    }
                }

                for (list in listsServer.lists) {
                    if (list.id !in setLocal) {
                        Log.d("MainActivity", "delete list:${list.id}")
                        todoService.deleteList(list.id, userServer.hash)
                    }
                }
                userDao.updateUserState(userLocal.username, true)


            }
        }
    }

}