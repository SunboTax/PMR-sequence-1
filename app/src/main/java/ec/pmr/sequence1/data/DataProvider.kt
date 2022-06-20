package ec.pmr.sequence1.data

import android.app.Application
import android.util.Log
import ec.pmr.sequence1.data.api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class DataProvider(baseUrl:String) {

    private val baseUrl = baseUrl
    private val CAT = "DataProvider"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val todoService = retrofit.create<TodoService>()

    suspend fun getUser(username: String, password: String): User {
        Log.d(CAT, "getting user info")
        return todoService.login(username, password)
    }

    suspend fun addUser(username: String, password: String): User{
        return todoService.addUser(username,password)
    }

    suspend fun getAllLists(token:String):Lists{
        return todoService.getLists(token)
    }

    suspend fun addList(label:String,token: String):ListOperation{
        return todoService.addList(label,token)
    }

    suspend fun deleteList(listId: Int,token: String){
        todoService.deleteList(listId, token)
    }

    suspend fun getAllItems(token: String,listId:Int):Items{
        return todoService.getItems(listId,token)
    }

    suspend fun addItem(listId: Int,token: String,label: String):ItemOperation{
        return todoService.addItem(listId, label, token)
    }

    suspend fun changeItem(listId: Int,itemId:Int,isChecked:String,token:String):ItemOperation{
        return todoService.changeItem(listId, itemId, isChecked, token)
    }

    suspend fun deletItem(listId: Int,itemId: Int,token: String){
        todoService.deleteItem(listId, itemId, token)
    }
}