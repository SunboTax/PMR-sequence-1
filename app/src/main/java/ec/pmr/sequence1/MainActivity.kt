package ec.pmr.sequence1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import ec.pmr.sequence1.model.ProfilListeTodo

class MainActivity : AppCompatActivity(){

    init {
        Log.d("inMain", "first of all")
    }


    //default preference
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    //restored preference
    lateinit var storedSharedPreferences: SharedPreferences
    lateinit var storedEditor: SharedPreferences.Editor
    val gson = Gson()

    //the profile held during the main activity
    lateinit var profile:ProfilListeTodo

    //define the requestCode to different activities
    val requestCodeToChoixListActivity: Int = 1

    lateinit var edtPseudoMain:EditText
    lateinit var  btnOkMain:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        edtPseudoMain = findViewById<EditText>(R.id.edt_Pseudo_Main)
        btnOkMain = findViewById<Button>(R.id.btn_Ok_Main)
        Log.d("inMain","onCreate")

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sharedPreferences.edit()

        //create a new file to restore user info or read a existed file of user info
        storedSharedPreferences = getSharedPreferences("sharedData", MODE_PRIVATE)
        storedEditor = storedSharedPreferences.edit()

        //autofill the latest used user pseudo
        if(this.sharedPreferences.getBoolean("default_enabled",true)) {
            val autoFill = this.sharedPreferences.getString("default_login_pseudo", "")
            edtPseudoMain.setText(autoFill)
        }

        //jump to the ChoixListActivity interface when clicking "ok" button
        btnOkMain.setOnClickListener{

            Log.d("inMain","Listening")
            Log.d("inMain",edtPseudoMain.text.toString())
            if(edtPseudoMain.text.toString() == ""){
                Toast.makeText(this@MainActivity,"invalid enter",Toast.LENGTH_SHORT).show()
            }else {
                //restore the user pseudo
                editor.putString("default_login_pseudo", edtPseudoMain.text.toString())
                editor.commit()

                //check if the pseudo is in the user info
                val userProfile = storedSharedPreferences.getString(edtPseudoMain.text.toString(), "")
                if (userProfile == "") {
                    profile = ProfilListeTodo(edtPseudoMain.text.toString())
                } else {
                    profile = gson.fromJson(userProfile, ProfilListeTodo::class.java)
                }


                //jump to choixlist activity
                val toChoixListActivity = Intent(this, ChoixListActivity::class.java)
                Log.d("inMain", "startLogin")
                toChoixListActivity.putExtra("user", this.profile)
                startActivityForResult(toChoixListActivity, requestCodeToChoixListActivity)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("inMain","onStart")

        //autofill the latest used user pseudo
        if(this.sharedPreferences.getBoolean("default_enabled",true)) {
            val autoFill = this.sharedPreferences.getString("default_login_pseudo", "")
            edtPseudoMain.setText(autoFill)
        }else{
            edtPseudoMain.setText("")
        }
    }

    override fun onRestart() {
        super.onRestart()
    }

    //the menu of preference setting realised by Action Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("inMain","onCreateOptionMenu")
        menuInflater.inflate(R.menu.choix_menu,menu)
        return true
    }

    //Once the menu is clicked, jump to the SettingActivity interface
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("inMain","onOptionsItemSelected")
        if(item.itemId ==R.id.Preference) {
            val verPreference = Intent(this,SettingActivity::class.java)
            startActivity(verPreference)
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

    //receive the data after click "<-" in the activity ChoixListActivity
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //according to the value of requestCode, we can know where the data come from
        when(requestCode){requestCode->
            if(resultCode== RESULT_OK){
                val receivedData = data?.getSerializableExtra("user") as ProfilListeTodo
                this.profile = receivedData
            }
        }

        //save any possible change made in the child activity
        this.storedEditor.putString(edtPseudoMain.text.toString(),gson.toJson(this.profile))
        this.storedEditor.commit()
        Log.d("inMain","user info restored")
    }
}


