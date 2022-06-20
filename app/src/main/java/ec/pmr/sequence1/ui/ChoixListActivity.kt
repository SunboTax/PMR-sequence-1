package ec.pmr.sequence1.ui

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
import ec.pmr.sequence1.data.api.ListOperation
import ec.pmr.sequence1.data.api.Lists
import ec.pmr.sequence1.data.api.TodoList
import ec.pmr.sequence1.ui.adapter.ListAdapter
import ec.pmr.sequence1.ui.model.user.ListeToDo
import kotlinx.coroutines.*

class ChoixListActivity : AppCompatActivity() {

    private val requestCodeToShowListActivity = 2
    private val CAT:String ="ChoixActivity"


    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkInfo: NetworkInfo
    private lateinit var adapter: ListAdapter
    private lateinit var lists: ArrayList<TodoList>
    private lateinit var edtListLabel: EditText
    private lateinit var btnOK: Button
    private lateinit var listBtn: RecyclerView

    private lateinit var url:String
    private lateinit var token:String
    private lateinit var username:String

    private lateinit var dataProvider: DataProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        Log.d(CAT,"onCreate")

        connectivityManager= getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkInfo = connectivityManager.activeNetworkInfo as NetworkInfo

        edtListLabel = findViewById<EditText>(R.id.edt_newList_Choix)
        btnOK = findViewById<Button>(R.id.btn_OK_Choix)
        listBtn = findViewById(R.id.recycle_Choix)

        val intent= intent
        url = intent.getStringExtra("url") as String
        token = intent.getStringExtra("token") as String
        username = intent.getStringExtra("username") as String

        dataProvider = DataProvider(url)

        this.title = "$username's lists"

        lists = ArrayList()
        showLists(token)
    }

    private val choixActivityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private fun showLists(token: String){

        //verify if the internet is connected
        if(networkInfo.isConnected == false){
            Toast.makeText(this@ChoixListActivity,"No Internet connection",Toast.LENGTH_SHORT).show()
        }else {
            Log.d(CAT, "loadLists")
            choixActivityScope.launch {
                loadAllLists(token)
            }
            adapter = ListAdapter(lists)

            Log.d(CAT,"showLists")
            listBtn.adapter = adapter
            listBtn.layoutManager = LinearLayoutManager(this@ChoixListActivity, RecyclerView.VERTICAL, false)

            adapter.setOnButtonClickListener(object : ListAdapter.OnButtonClickListener {
                override fun onItemClick(position: Int) {
                    toShowListActivity(position)
                }
            })

            adapter.setOnImageButtonClickListener(object :ListAdapter.OnImageButtonClickListener{
                override fun onItemClick(position: Int) {
                    choixActivityScope.launch {
                        try{
                            deleteList(lists[position].id,token)
                            adapter.deleteList(position)
                            lists.removeAt(position)
                        }catch (exception:Exception){
                            Log.d(CAT,exception.toString())
                        }
                    }
                }
            })

            var label =""
            btnOK.setOnClickListener{
                Log.d(CAT,"add a new list")
                label = edtListLabel.text.toString()
                if(label == ""){
                    Toast.makeText(this@ChoixListActivity, "invalid enter", Toast.LENGTH_SHORT)
                        .show()
                }else if(inLists(label)){
                    Toast.makeText(this@ChoixListActivity, "list exited", Toast.LENGTH_SHORT)
                        .show()
                    label = ""
                }else{
                    choixActivityScope.launch {
                        try {
                            val list: ListOperation = addList(label, token) as ListOperation
                            if (list.success) {
                                lists.add(list.todoList)
                                adapter.addList(list.todoList)
                                Toast.makeText(
                                    this@ChoixListActivity,
                                    "operation succeeded",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@ChoixListActivity,
                                    "operation failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }catch (exception:Exception) {
                            Log.d(CAT,exception.toString())
                        }
                    }
                }
                edtListLabel.setText("")
            }
        }
    }

    private suspend fun loadAllLists(token: String) {
        lists = dataProvider.getAllLists(token).toArrayList()
        for (it in lists) {
            adapter.addList(it)
            Log.d(CAT, it.toString())
        }
        Log.d(CAT, lists.size.toString())
    }

    private fun Lists.toArrayList():ArrayList<TodoList>{
        val arrayList:ArrayList<TodoList> = ArrayList()
        for(it in this.lists){
            arrayList.add(it)
        }
        return arrayList
    }

    private suspend fun addList(label: String,token: String):ListOperation? {
        return if (label == "") {
            null
        } else {
            dataProvider.addList(label, token)
        }
    }

    private fun inLists(label: String): Boolean{
        for(it in lists){
            if(it.label == label){
                return true
            }
        }
        return false
    }

    private suspend fun deleteList(listId: Int, token: String){
        dataProvider.deleteList(listId, token)
    }

    private fun toShowListActivity(position:Int){

        Log.d(CAT,"startJumping")
        //jump to the ShowListActivity interface when click a list
        val toShowListActivity = Intent(this@ChoixListActivity, ShowListActivity::class.java)
        toShowListActivity.putExtra("url",url)
        toShowListActivity.putExtra("token", token)
        toShowListActivity.putExtra("username", username)
        toShowListActivity.putExtra("listId",lists[position].id)
        toShowListActivity.putExtra("label",lists[position].label)
        startActivity(toShowListActivity)
    }

}