package ec.pmr.sequence1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import ec.pmr.sequence1.ChoixListActivity as ChoixListActivity

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        Log.d("Main","running")
        val refEdtPesudo = findViewById<EditText>(R.id.editText)
        val refButton = findViewById<Button>(R.id.buTTon)

        //jump to the ChoixListActivity interface when clicking "ok" button
        refButton.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val verChoixListActivity = Intent(this@MainActivity,ChoixListActivity::class.java)
                Log.d("Main","reading")
//                val dataSet:List<Item> = readDataSet("user_dataset.xml")
//                if(isNewUser(dataSet,refEdtPesudo.text.toString())){
//                    Log.d("Main","creating")
//                    createUserInfo(refEdtPesudo.text.toString(), "user_dataset.xml")
//                }

                Log.d("Main",refEdtPesudo.text.toString())
                //transfer user info to the next activity
                verChoixListActivity.putExtra("userPseudo",refEdtPesudo.text.toString())
                Log.d("Main","jumping")
                startActivity(verChoixListActivity)
            }
        })

    }

    /* check if the pseudo entered is in the user dataset
    *? return true if it's a new user
    */
    fun isNewUser(dataSet:List<Item>,pseudo:String):Boolean{
        for(it in dataSet){
            if(pseudo == it.item){
                return false
            }
        }
        return true
    }

//    fun createUserInfo(pseudo: String,filename: String){
//        val dbf = DocumentBuilderFactory.newInstance()
//        Log.d("Main","5")
//        val db = dbf.newDocumentBuilder()
//        Log.d("Main","6")
//        val inputStream: InputStream = assets.open(filename)
//        val doc = db.parse(inputStream)
//        Log.d("Main","7")
//        val root = doc.documentElement
//        val newUser = doc.createElement("user")
//
//        newUser.setAttribute("name",pseudo)
//        root.appendChild(newUser)
//        try {
//            val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
//            Log.d("Main", "adding")
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
//            Log.d("Main", "1")
//            transformer.setOutputProperty(OutputKeys.METHOD, "xml")
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
//            Log.d("Main", Paths.get("").toAbsolutePath().toString())
//            val fileNames = mutableSetOf<String>()
//            val filetree:FileTreeWalk = File(Paths.get("").toAbsolutePath().toString()).walk()
//
//            filetree.maxDepth(1)
//                .filter { it.isDirectory }
//                .forEach { fileNames.add(it.name) }
//            for(it in fileNames) {
//                Log.d("Main","dirName"+it.toString())
//            }
//            transformer.transform(DOMSource(doc), StreamResult("user_dataset.xml"))
//            Log.d("Main", "created")
//        }
//        catch (te: TransformerException){
//            Log.d("Main","te:"+te.message.toString())
//        }
//        catch (ioe:IOException){
//            Log.d("Main","io:"+ioe.message.toString())
//        }
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


