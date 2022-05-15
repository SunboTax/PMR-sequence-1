package ec.pmr.sequence1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val list = findViewById<RecyclerView>(R.id.list)
        val dataSet = readDataSet("user_dataset.xml")
        Log.d("dataset",dataSet.toString())
        list.adapter = ItemAdapter(dataSet)
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.operation_pseudo,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==R.id.Delete) {
            Toast.makeText(this,"All pseudos deleted",Toast.LENGTH_LONG).show()
//            clearHistory("user_dataset.xml")
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

//    fun clearHistory(filename: String){
//        Log.d("clear","starting")
//        val dbf = DocumentBuilderFactory.newInstance()
//        val xmlPseudo:InputStream = assets.open(filename)
//        val db = dbf.newDocumentBuilder()
//        val doc = db.parse(xmlPseudo)
//
//        val childList:NodeList = doc.getElementsByTagName("user")
//        var user:Node = childList.item(0)
//        user.parentNode.removeChild(user)
//
//
//        Log.d("childNode", childList.length.toString())
//    }



    fun readDataSet(filename:String):List<Item>{
        val result = mutableListOf<Item>()
        val dbf = DocumentBuilderFactory.newInstance()
        val inputStream: InputStream = assets.open(filename)
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(inputStream)
        val userNodes: NodeList = doc.getElementsByTagName("user")
        for(it in 0 until userNodes.length){
            val userNode = userNodes.item(it) as Element
            result.add(Item(item = userNode.getAttribute("name").toString()))
        }
        return result
    }

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
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pseudo, parent, false)
            return ItemViewHolder(itemView = itemView)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Log.d("bind",dataSet.toString())
            holder.bind(item = dataSet[position])
        }
    }

    data class Item(
        val item: String
    )

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pseudo = itemView.findViewById<TextView>(R.id.pseudo)

        fun bind(item: Item) {
            pseudo.text = item.item
        }
    }
}



