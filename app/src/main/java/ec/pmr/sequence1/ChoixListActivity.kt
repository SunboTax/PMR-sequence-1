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
import ec.pmr.sequence1.adapter.ListAdapter
import ec.pmr.sequence1.model.ListeToDo
import ec.pmr.sequence1.model.ProfilListeTodo

class ChoixListActivity : AppCompatActivity() {

    //the profile held during the choixList activity
    lateinit var profile:ProfilListeTodo

    val requestCodeToShowListActivity = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        Log.d("Choix","onCreate")
        //receive the info transfered
        val intent= intent
        profile = intent.getSerializableExtra("user") as ProfilListeTodo
        this.title = profile.getLogin()
    }

    override fun onStart() {
        super.onStart()

        Log.d("Choix",profile.toString())
        //show all list titles
        val listBtn = findViewById<RecyclerView>(R.id.recycle_Choix)
        val dataset: ArrayList<ListeToDo> ?= profile.getMesListeToDo()
        Log.d("Choix",dataset.toString())


        val adapter= ListAdapter(dataset as ArrayList<ListeToDo>)
        listBtn.adapter = adapter
        listBtn.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter.setOnButtonClickListener(object:ListAdapter.onButtonClickListener{
            override fun onItemClick(position: Int) {
                val toShowListActivity = Intent(this@ChoixListActivity,ShowListActivity::class.java)
                toShowListActivity.putExtra("user",this@ChoixListActivity.profile)
                toShowListActivity.putExtra("list",dataset[position])
                startActivityForResult(toShowListActivity,requestCodeToShowListActivity)
            }
        })

        //add new list item
        val editText = findViewById<EditText>(R.id.edt_newList_Choix)
        val btn = findViewById<Button>(R.id.btn_OK_Choix)
        btn.setOnClickListener{
            Log.d("Choix","add new list")
            if(editText.text.toString() == ""){
                Toast.makeText(this,"invalid enter",Toast.LENGTH_SHORT).show()
            }else {
                if (profile.inMesListeToDo(editText.text.toString())) {
                    Toast.makeText(this, "list existed", Toast.LENGTH_SHORT).show()
                } else {
                    profile.ajouteListe(ListeToDo(editText.text.toString()))
                    adapter.addList(editText.text.toString())
                }
            }
            editText.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choix_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==R.id.Preference) {
            val verPreference = Intent(this@ChoixListActivity,SettingActivity::class.java)
            startActivity(verPreference)
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //according to the value of requestCode, we can know where the data come from
        when(requestCode){requestCode->
            if(resultCode== RESULT_OK){
                val receivedData = data?.getSerializableExtra("list") as ListeToDo
                Log.d("Choix",receivedData.toString())
                this.profile.refreshList(receivedData)
                Log.d("Choix",profile.toString())
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("user",this.profile)
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }
}