package ec.pmr.sequence1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import ec.pmr.sequence1.model.ProfilListeTodo
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import ec.pmr.sequence1.ChoixListActivity as ChoixListActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val edtPseudoMain = findViewById<EditText>(R.id.edt_Pseudo_Main)
        val btnOkMain = findViewById<Button>(R.id.btn_Ok_Main)

        //jump to the ChoixListActivity interface when clicking "ok" button
        btnOkMain.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val verChoixListActivity = Intent(this@MainActivity,ChoixListActivity::class.java)
                Log.d("inMain","startLogin")
                val filename = "ec/pmr/sequence1/data/user_dataset.xml"
                if(isNewUser(edtPseudoMain.text.toString(),filename)){
                    createUserInfo(edtPseudoMain.text.toString(),filename)
                }

                //transfer user info to the next activity
                verChoixListActivity.putExtra("userPseduo",edtPseudoMain.text.toString())
                startActivity(verChoixListActivity)
            }
        })

    }

    /* check if the pseudo entered is in the user dataset
    *? return true if it's a new user
    */
    fun isNewUser(pseudo:String,filename: String):Boolean{
        val dataSet:List<Item> = readDataSet(filename)
        for(it in dataSet){
            if(pseudo == it.item){
                return false
            }
        }
        return true
    }

    //create new user info in the dataset
    fun createUserInfo(pseudo: String,filename: String){
        val dbf = DocumentBuilderFactory.newInstance()
        Log.d("inMain","5")
        val db = dbf.newDocumentBuilder()
        Log.d("inMain","6")
        val inputStream: InputStream = assets.open(filename)
        val doc = db.parse(inputStream)
        Log.d("inMain","7")
        val root = doc.documentElement
        val newUser = doc.createElement("user")

        newUser.setAttribute("name",pseudo)
        root.appendChild(newUser)
        try {
            val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
            Log.d("inMain", "adding")
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            Log.d("inMain", "1")
            transformer.setOutputProperty(OutputKeys.METHOD, "xml")
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")

            transformer.transform(DOMSource(doc), StreamResult(File(filename)))
            Log.d("inMain", "created")
        }
        catch (te: TransformerException){
            Log.d("inMain","te:"+te.message.toString())
        }
        catch (ioe: IOException){
            Log.d("inMain","io:"+ioe.message.toString())
        }
    }

    fun readDataSet(filename:String):List<Item>{
        Log.d("inMain","reading")
        val result = mutableListOf<Item>()
        val dbf = DocumentBuilderFactory.newInstance()
        try {
            val inputStream: InputStream = FileInputStream(File(filename))
            val db = dbf.newDocumentBuilder()
            val doc = db.parse(inputStream)
            val userNodes: NodeList = doc.getElementsByTagName("user")
            for(it in 0 until userNodes.length){
                val userNode = userNodes.item(it) as Element
                result.add(Item(item = userNode.getAttribute("name").toString()))
            }

        }
        catch (FI:IOException){
            Log.d("inMain",FI.toString())
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


