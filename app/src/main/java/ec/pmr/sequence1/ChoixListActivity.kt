package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class ChoixListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("onCreateChoix","starting")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        //receive the info transfered by the
        val intent= intent
        val userPseudo:String = intent.getStringExtra("userPseudo").toString()
        Log.d("onCreateChoix",userPseudo)
        Log.d("onCreateChoix","searching list")
        val listBtn = findViewById<RecyclerView>(R.id.listBtn)
        val dataSet = readUserInfo("user_dataset.xml",userPseudo)
        val adapter= ItemAdapter(dataSet)

        listBtn.adapter = adapter
        listBtn.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        Log.d("onCreateChoix","all lists")

        adapter.setOnButtonClickListener(object:ItemAdapter.onButtonClickListener{
            override fun onItemClick(position: Int) {
                Log.d("onCreateChoix","listening")
                val verChoixListActivity = Intent(this@ChoixListActivity,ShowListActivity::class.java)
                Log.d("onCreateChoix","listItem:"+adapter.buttonListener.toString())
                val itemTitres:Array<String> = getItemUnderList("user_dataset.xml",userPseudo,dataSet[position].item)
                verChoixListActivity.putExtra("itemTitres",itemTitres)
                startActivity(verChoixListActivity)
            }
        })
    }

    //get the lists of the user
    fun readUserInfo(filename:String,userPseudo:String):List<Item>{
        val result = mutableListOf<Item>()
        val dbf = DocumentBuilderFactory.newInstance()
        val xmlPseudo: InputStream = assets.open(filename)
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(xmlPseudo)

        val userNodes: NodeList = doc.getElementsByTagName("user")
        for(it in 0 until userNodes.length){
            Log.d("onCreateChoix","it:"+it.toString())
            val userNode = userNodes.item(it) as Element
            Log.d("onCreateChoix",userNode.getAttribute("name").toString())
            if(userNode.getAttribute("name").toString().equals(userPseudo)) {
                val lists = userNode.getElementsByTagName("list")
                for(i in 0 until lists.length){
                    val list = lists.item(i) as Element
                    Log.d("onCreateChoix",list.getAttribute("name").toString())
                    result.add(Item(item = list.getAttribute("name").toString()))
                }
                return result
            }
        }
        Log.d("onCreateChoix","empty")
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
        var it = 0
        while(it < userNodes.length){
            val userNode = userNodes.item(it) as Element
            if(userNode.getAttribute("name").toString() == userPseudo) {
                break
            }
            it += 1
        }


        val userNode = userNodes.item(it) as Element
        val lists = userNode.getElementsByTagName("list")
        var i=0
        while(i < lists.length){
            val list = lists.item(i) as Element
            if(list.getAttribute("name").toString() == listTitreString) {
                break
            }
            i += 1
        }

        val list = lists.item(i) as Element
        val items = list.getElementsByTagName("listTitre")
        var j=0
        while (j<lists.length){
            val item = items.item(i) as Element
            result = result.plus(item.getAttribute("name").toString())
            j += 1
        }

        return result
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
            Log.d("getSize",int.toString())
            return int
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            Log.d("onCreateChoix","ViewCreated")
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            return ItemViewHolder(itemView = itemView,buttonListener as onButtonClickListener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Log.d("onCreateChoix",dataSet.toString())
            holder.bind(item = dataSet[position])
        }

        interface onButtonClickListener{
            fun onItemClick(position:Int)
        }

        fun setOnButtonClickListener(listner:onButtonClickListener){
            Log.d("onCreateChoix","setListener")
            buttonListener = listner
        }


    }

    class ItemViewHolder(itemView: View,listner: ItemAdapter.onButtonClickListener) : RecyclerView.ViewHolder(itemView) {
        private val listTitre:Button = itemView.findViewById<Button>(R.id.Btn)

        init {
            itemView.setOnClickListener {
                Log.d("onCreateChoix", "listner")
                listner.onItemClick(position)
            }
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