package ec.pmr.sequence1.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import ec.pmr.sequence1.R
import ec.pmr.sequence1.data.DataProvider
import ec.pmr.sequence1.data.api.User
import ec.pmr.sequence1.ui.model.user.ProfilListeTodo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){

    //define the requestCode to different activities
    private val requestCodeToChoixListActivity: Int = 1
    private val CAT:String ="MainActivity"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var url:String
    private lateinit var dataProvider: DataProvider
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkInfo:NetworkInfo
    private lateinit var edtUsername:EditText
    private lateinit var edtPassword:EditText
    private lateinit var  btnLoginMain:Button
    private lateinit var  btnSigninMain:Button
    private lateinit var loader:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        url = sharedPreferences.getString("default_url", "http://tomnab.fr/todo-api/") as String
        Log.d(CAT,url)
        dataProvider = DataProvider(url)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkInfo = connectivityManager.activeNetworkInfo as NetworkInfo

        edtUsername = findViewById(R.id.edt_username_Main)
        edtPassword = findViewById(R.id.edt_password_Main)
        btnLoginMain = findViewById(R.id.btn_Login_Main)
        btnSigninMain = findViewById(R.id.btn_Signin_Main)
        loader = findViewById(R.id.loader)

        loader.visibility = View.GONE

        Log.d(CAT,"onCreate")
        btnLoginMain.setOnClickListener {
            if (edtUsername.text.toString() == "") {
                Toast.makeText(this@MainActivity, "please enter your username", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtPassword.text.toString() == "") {
                Toast.makeText(this@MainActivity, "please enter the password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                login(edtUsername.text.toString(), edtPassword.text.toString())
            }
        }

        btnSigninMain.setOnClickListener{
            if (edtUsername.text.toString() == "") {
                Toast.makeText(this@MainActivity, "please enter your username", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtPassword.text.toString() == "") {
                Toast.makeText(this@MainActivity, "please enter the password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                signin(edtUsername.text.toString(), edtPassword.text.toString())
            }
        }
    }

    //the menu of preference setting realised by Action Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(CAT,"onCreateOptionMenu")
        menuInflater.inflate(R.menu.choix_menu,menu)
        return true
    }

    //Once the menu is clicked, jump to the SettingActivity interface
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(CAT,"onOptionsItemSelected")
        if(item.itemId == R.id.Preference) {
            val verPreference = Intent(this, SettingActivity::class.java)
            startActivity(verPreference)
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }

    private val mainActivityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private fun login(username:String, password:String) {

        Log.d(CAT,"onConnection")

        //verify if the internet is connected
        if (networkInfo?.isConnected == false) {
            Toast.makeText(this@MainActivity, "No Internet connection", Toast.LENGTH_SHORT).show()
        } else {
            mainActivityScope.launch {
                loader.visibility = View.VISIBLE

                Log.d(CAT,"onLogin")
                val user = getUser(username, password)
                if (user.isLogin) {
                    Log.d(CAT,"login succeed")
                    toChoixListActivity(username, user.token)
                } else {
                    Log.d(CAT,"login failed")
                    loader.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        "username or password is not correct",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun signin(username:String, password:String){
        if(networkInfo?.isConnected == false){
            Toast.makeText(this@MainActivity,"No Internet connection",Toast.LENGTH_SHORT).show()
        }else {
            mainActivityScope.launch {
                loader.visibility = View.VISIBLE

                val user = addUser(username, password)
                if(user.isLogin){
                    Toast.makeText(this@MainActivity,"New account created ",Toast.LENGTH_SHORT).show()
                    toChoixListActivity(username,user.token)
                }else{
                    loader.visibility = View.GONE
                    Toast.makeText(this@MainActivity,"operation failed",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun getUser(username:String, password:String):User {
        return dataProvider.getUser(username, password)
    }

    private suspend fun addUser(username:String, password:String):User {
        return dataProvider.addUser(username, password)
    }

    private fun toChoixListActivity(username:String, token:String){
        //jump to the ChoixListActivity interface when the login succeed
        val toChoixListActivity = Intent(this, ChoixListActivity::class.java)
        Log.d(CAT, "startJumping")
        toChoixListActivity.putExtra("url",url)
        toChoixListActivity.putExtra("token", token)
        toChoixListActivity.putExtra("username", username)
        startActivity(toChoixListActivity)
    }
}


