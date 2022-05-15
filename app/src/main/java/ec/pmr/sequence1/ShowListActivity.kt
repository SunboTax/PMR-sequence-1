package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val intent = Intent()
        val itemTitres:Array<String> = intent.getStringArrayExtra("itemTitres") as Array<String>

        val dataSet = mutableListOf<Item>()
        for(it in itemTitres){
            dataSet.add(Item(item = it))
        }

        val listBtn = findViewById<RecyclerView>(R.id.listCB)

        listBtn.adapter = ItemAdapter(dataSet as List<Item>)
        listBtn.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    data class Item(
        val item:String
    )

    class ItemAdapter(
        private val dataSet: List<Item>
    ) : RecyclerView.Adapter<ItemViewHolder>() {

        override fun getItemCount(): Int {
            val int = dataSet.size
            Log.d("getSize",int.toString())
            return int
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            Log.d("Create","ViewCreated")
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ItemViewHolder(itemView = itemView)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Log.d("bind",dataSet.toString())
            holder.bind(item = dataSet[position])
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listTitre = itemView.findViewById<Button>(R.id.Btn)

        fun bind(item: Item) {
            listTitre.text = item.item
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