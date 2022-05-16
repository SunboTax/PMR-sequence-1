package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.FileInputStream
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class ChoixListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        //receive the info transfered by the
        val intent= intent
        val userPseudo:String = intent.getStringExtra("userPseudo").toString()
        val listBtn = findViewById<RecyclerView>(R.id.recycle_Choix)
        val dataSet = readUserInfo("user_dataset.xml",userPseudo)
        val adapter= ItemAdapter(dataSet)

        listBtn.adapter = adapter
        listBtn.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter.setOnButtonClickListener(object:ItemAdapter.onButtonClickListener{
            override fun onItemClick(position: Int) {
                val verShowListActivity = Intent(this@ChoixListActivity,ShowListActivity::class.java)
                val itemTitres:Array<String> = getItemUnderList("user_dataset.xml",userPseudo,dataSet[position].item)
                verShowListActivity.putExtra("itemTitres",itemTitres)
                startActivity(verShowListActivity)
            }
        })

        val editText = findViewById<EditText>(R.id.edt_newList_Choix)
        val btn = findViewById<Button>(R.id.btn_OK_Choix)
        btn.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                createList(editText.text.toString(),"user_dataset.xml")
            }
        })
    }

    //create new list for the user
    fun createList(listName:String,filename: String){
        TODO("manipulate the dataset")
    }

    //get the lists of the user
    fun readUserInfo(filename:String,userPseudo:String):List<Item>{
        val result = mutableListOf<Item>()
        val dbf = DocumentBuilderFactory.newInstance()
        val xmlPseudo: InputStream = FileInputStream(filename)
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(xmlPseudo)

        val userNodes: NodeList = doc.getElementsByTagName("user")
        for(it in 0 until userNodes.length){
            val userNode = userNodes.item(it) as Element
            if(userNode.getAttribute("name").toString().equals(userPseudo)) {
                val lists = userNode.getElementsByTagName("list")
                for(i in 0 until lists.length){
                    val list = lists.item(i) as Element
                    result.add(Item(item = list.getAttribute("name").toString()))
                }
                return result
            }
        }
        return emptyList()
    }

    //get the items of the selected list
    fun getItemUnderList(filename:String,userPseudo:String,listTitreString:String):Array<String>{
        var result = emptyArray<String>()
        val dbf = DocumentBuilderFactory.newInstance()
        val xmlPseudo: InputStream = assets.open(filename)
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(xmlPseudo)

        val userNodes: NodeList = doc.getElementsByTagName("user")
        for(it in 0 until userNodes.length){
            val userNode = userNodes.item(it) as Element
            if(userNode.getAttribute("name").toString() == userPseudo) {
                val lists = userNode.getElementsByTagName("list")
                for(i in 0 until lists.length){
                    val list = lists.item(i) as Element
                    if(list.getAttribute("name").toString() == listTitreString) {
                        val items = list.getElementsByTagName("item")
                        for(j in 0 until items.length){
                            val item = items.item(j) as Element
                            result = result.plus(item.getAttribute("name").toString())
                        }

                        return result
                    }
                }
            }
        }
        return emptyArray()

    }

    data class Item(
        val item:String
    )

    class ItemAdapter(
        private val dataSet: List<Item>
    ) : RecyclerView.Adapter<ItemViewHolder>() {

        var buttonListener:onButtonClickListener? = null

        override fun getItemCount(): Int {
            val int = dataSet.size
            return int
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ItemViewHolder(itemView = itemView,buttonListener as onButtonClickListener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(item = dataSet[position])
        }

        interface onButtonClickListener{
            fun onItemClick(position:Int)
        }

        fun setOnButtonClickListener(listner:onButtonClickListener){
            buttonListener = listner
        }


    }

    class ItemViewHolder(itemView: View,listner: ItemAdapter.onButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        private val listTitre:Button = itemView.findViewById<Button>(R.id.btn_List_Choix)

        init {
            listTitre.setOnClickListener(object:View.OnClickListener{
                override fun onClick(v: View?) {
                    listner.onItemClick(adapterPosition)
                }

            })
        }

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
            val verPreference = Intent(this@ChoixListActivity,SettingActivity::class.java)
            startActivity(verPreference)
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }
}