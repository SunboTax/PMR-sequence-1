package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val intent = intent
        val itemTitres:Array<String> = intent.getStringArrayExtra("itemTitres") as Array<String>

        val dataSet = mutableListOf<Item>()
        for(element in itemTitres){
            dataSet.add(Item(item = element))
        }

        val listBtn = findViewById<RecyclerView>(R.id.recycle_Show)

        listBtn.adapter = ItemAdapter(dataSet as List<Item>)
        listBtn.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val editText = findViewById<EditText>(R.id.edt_newList_Show)
        val btn = findViewById<Button>(R.id.btn_OK_Show)
        btn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                createList(editText.text.toString(),"user_dataset.xml")
            }
        })
    }

    //create new item in the list for the user
    fun createList(listName:String,filename: String){
        TODO("manipulate the dataset")
    }

    data class Item(
        val item:String
    )

    class ItemAdapter(
        private val dataSet: List<Item>
    ) : RecyclerView.Adapter<ItemViewHolder>() {

        override fun getItemCount(): Int {
            val int = dataSet.size
            return int
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_switch, parent, false)
            return ItemViewHolder(itemView = itemView)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(item = dataSet[position])
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemTitre = itemView.findViewById<Button>(R.id.switch_CheckBox_Show)

        fun bind(item: Item) {
            itemTitre.text = item.item
        }
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
}