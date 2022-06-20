package ec.pmr.sequence1.ui

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ec.pmr.sequence1.R
import ec.pmr.sequence1.data.DataProvider
import ec.pmr.sequence1.data.api.ItemOperation
import ec.pmr.sequence1.data.api.Items
import ec.pmr.sequence1.data.api.ListOperation
import ec.pmr.sequence1.data.api.TodoItem
import ec.pmr.sequence1.ui.adapter.ItemAdapter
import ec.pmr.sequence1.ui.model.user.ItemToDo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.text.FieldPosition

class ShowListActivity : AppCompatActivity() {

    private var listId:Int = -1
    private var CAT = "ShowActivity"

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkInfo:NetworkInfo
    private lateinit var url:String
    private lateinit var token:String
    private lateinit var username:String
    private lateinit var label:String
    private lateinit var adapter: ItemAdapter
    private lateinit var items:ArrayList<TodoItem>
    private lateinit var edtItemLabel:EditText
    private lateinit var btnOk:Button
    private lateinit var listBtn:RecyclerView


    private val dataProvider: DataProvider by lazy { DataProvider(url) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkInfo = connectivityManager.activeNetworkInfo as NetworkInfo

        items = ArrayList()

        val intent = intent
        url = intent.getStringExtra("url") as String
        token = intent.getStringExtra("token") as String
        username = intent.getStringExtra("username") as String
        listId = intent.getIntExtra("listId",-1)
        label = intent.getStringExtra("label") as String

        this.title = "$username's $label"

        edtItemLabel = findViewById(R.id.edt_newList_Show)
        btnOk = findViewById(R.id.btn_OK_Show)
        listBtn = findViewById(R.id.recycle_Show)

        showItems(token,listId)
    }

    private val showActivityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private fun showItems(token:String,listId:Int) {
        if (!networkInfo.isConnected) {
            Toast.makeText(this@ShowListActivity, "No Internet connection", Toast.LENGTH_SHORT)
                .show()
        } else {
            showActivityScope.launch {
                getAllItems(token, listId)
            }

            adapter = ItemAdapter(items)
            listBtn.adapter = adapter
            listBtn.layoutManager = LinearLayoutManager(this@ShowListActivity, RecyclerView.VERTICAL, false)

            adapter.setOnCheckBoxClickListener(object :ItemAdapter.OnCheckBoxClickListener{
                override fun onItemClick(position: Int) {
                    if(items[position].isChecked == "1"){
                        items[position].isChecked = "0"
                    }else{
                        items[position].isChecked = "1"
                    }
                    showActivityScope.launch {
                        try {

                            if(items[position].isChecked == "1") {
                                changeItem(listId, adapter.dataSet[position].id, "1", token)
                            }else{
                                changeItem(listId, adapter.dataSet[position].id, "0", token)
                            }
                        }catch (exception:Exception){
                            Log.d(CAT,exception.toString())
                        }
                    }
                }
            })

            adapter.setOnButtonClickListener(object :ItemAdapter.OnButtonClickListener{
                override fun onItemClick(position: Int) {
                    showActivityScope.launch {
                        try {
                            Log.d(CAT,position.toString())
                            deleteItem(listId, items[position].id, token)
                            adapter.deleteItem(position)
                            items.removeAt(position)
                        }catch (exception:Exception){
                            Log.d(CAT,exception.toString())
                        }
                    }

                }
            })

            var label = ""
            btnOk.setOnClickListener {
                label = edtItemLabel.text.toString()
                if (label == "") {
                    Toast.makeText(this@ShowListActivity, "invalid enter", Toast.LENGTH_SHORT).show()
                }else if (inItems(label)) {
                    Toast.makeText(this@ShowListActivity, "item exited", Toast.LENGTH_SHORT).show()
                    label = ""
                }else{
                    showActivityScope.launch {
                        try {
                            val item: ItemOperation = addItem(listId, label, token) as ItemOperation
                            if (item.success) {
                                Log.d(CAT, item.todoItem.toString())
                                adapter.addItem(item.todoItem)
                                items.add(item.todoItem)
                                Toast.makeText(
                                    this@ShowListActivity,
                                    "operation succeeded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@ShowListActivity,
                                    "operation failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }catch (exception:Exception){
                            Log.d(CAT,exception.toString())
                        }
                    }
                    edtItemLabel.setText("")
                }
            }
        }
    }

    private suspend fun getAllItems(token:String, listId:Int){
        items = dataProvider.getAllItems(token, listId).toArrayList()
        for(it in items){
            adapter.addItem(it)
        }
        Log.d(CAT,"item number:${items.toString()}")
    }

    private fun Items.toArrayList(): ArrayList<TodoItem> {
        val arrayList:ArrayList<TodoItem> = ArrayList()
        for(it in this.items){
            arrayList.add(it)
        }
        return arrayList
    }

    private suspend fun addItem(listId: Int,label: String,token: String): ItemOperation? {
        return if (label == "") {
            null
        } else {
            dataProvider.addItem(listId, token, label)
        }
    }

    private fun inItems(label: String): Boolean{
        for(it in items){
            if(it.label == label){
                return true
            }
        }
        return false
    }

    private suspend fun changeItem(listId: Int,itemId:Int,isChecked:String,token: String):ItemOperation{
        return dataProvider.changeItem(listId, itemId, isChecked, token)
    }

    private suspend fun deleteItem(listId: Int,itemId: Int,token: String){
        dataProvider.deletItem(listId, itemId, token)
    }

}