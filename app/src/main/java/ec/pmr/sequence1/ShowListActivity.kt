package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ec.pmr.sequence1.adapter.ItemAdapter
import ec.pmr.sequence1.model.ItemToDo
import ec.pmr.sequence1.model.ListeToDo
import ec.pmr.sequence1.model.ProfilListeTodo

class ShowListActivity : AppCompatActivity() {

    lateinit var profile: ProfilListeTodo
    lateinit var list: ListeToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)


        Log.d("Show","onCreate")
        val intent = intent
        list = intent.getSerializableExtra("list") as ListeToDo
        profile = intent.getSerializableExtra("user") as ProfilListeTodo
        this.title = profile.getLogin()+"-"+list.getTitreListeToDo()

        val dataSet:ArrayList<ItemToDo> ?= list.getLesItems()
        val listBtn = findViewById<RecyclerView>(R.id.recycle_Show)

        val adapter = ItemAdapter(dataSet as ArrayList<ItemToDo> )
        listBtn.adapter = adapter
        listBtn.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val editText = findViewById<EditText>(R.id.edt_newList_Show)
        val btn = findViewById<Button>(R.id.btn_OK_Show)

        btn.setOnClickListener{
            Log.d("Show",editText.text.toString())
            if(editText.text.toString()==""){
                Toast.makeText(this,"invalid enter",Toast.LENGTH_SHORT).show()
            }else{
                if(list.inLesItems(editText.text.toString())) {
                    Toast.makeText(this,"item existed",Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("Show","add new item")
                    list.ajouteItem(ItemToDo(editText.text.toString()))
                    adapter.addItem(editText.text.toString())
                }
            }
            editText.setText("")
        }

        adapter.setOnCheckBoxClickListener(object :ItemAdapter.onChekcBoxClickListener{
            override fun onItemClick(position: Int) {
                list.getLesItems()[position].changeFait()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choix_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==R.id.Preference) {
            val verPreference = Intent(this@ShowListActivity,SettingActivity::class.java)
            startActivity(verPreference)
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("list",this.list)
        Log.d("Show",list.toString())
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }
}