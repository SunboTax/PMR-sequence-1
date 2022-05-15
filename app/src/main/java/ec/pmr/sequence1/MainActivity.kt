package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import ec.pmr.sequence1.ChoixListActivity as ChoixListActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val dataSet:List<Item> = readDataSet("user_dataSet.xml")

        val refEdtPesudo = findViewById<EditText>(R.id.editText)
        val refButton = findViewById<Button>(R.id.buTTon)

        //jump to the ChoixListActivity interface when clicking "ok" button
        refButton.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val verChoixListActivity = Intent(this@MainActivity,ChoixListActivity::class.java)

                //check if the pseudo entered is in the user dataset, if not, create a new user item
                for(it in dataSet){
                    if(it.item == refEdtPesudo.text.toString()){

                    }
                }
                //transfer user info to the next activity
                verChoixListActivity.putExtra("userPseudo",refEdtPesudo.text.toString())
                startActivity(verChoixListActivity)
            }
        })

    }

    fun readDataSet(filename:String):List<Item>{
        val result = mutableListOf<Item>()

        val dbf = DocumentBuilderFactory.newInstance()
        val xmlPseudo: InputStream = assets.open(filename)
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(xmlPseudo)

        val userNodes: NodeList = doc.getElementsByTagName("user")
        for(it in 0..(userNodes.length-1)){
            val userNode = userNodes.item(it) as Element
            val pseudo = userNode.getElementsByTagName("pseudo")
            result.add(Item(item = pseudo.item(0).firstChild.nodeValue))
        }
        return result
    }

    data class Item(
        val item:String
    )

    //the menu of preference setting realised by Action Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choix_menu,menu)
        return true
    }

    //Once the menu is clicked, jump to the SettingActivity interface
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId ==R.id.Preference) {
            val verPreference = Intent(this@MainActivity,SettingActivity::class.java)
            startActivity(verPreference)
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

}


