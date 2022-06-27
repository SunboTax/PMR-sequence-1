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
import ec.pmr.sequence1.data.api.response.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //define the requestCode to different activities
    private val requestCodeToChoixListActivity: Int = 1
    private val CAT: String = "MainActivity"
    private var connected = true

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var url: String
    private lateinit var token: String
    private lateinit var dataProvider: DataProvider
    private lateinit var connectivityManager: ConnectivityManager
    private var networkInfo: NetworkInfo? = null
    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLoginMain: Button
    private lateinit var btnSigninMain: Button
    private lateinit var loader: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sharedPreferences.edit()
        url = sharedPreferences.getString("default_url", "http://tomnab.fr/todo-api/") as String
        token = sharedPreferences.getString("token", "") as String
        Log.d(CAT, url)
        dataProvider = DataProvider(url, this.application)

        edtUsername = findViewById(R.id.edt_username_Main)
        edtPassword = findViewById(R.id.edt_password_Main)
        btnLoginMain = findViewById(R.id.btn_Login_Main)
        btnSigninMain = findViewById(R.id.btn_Signin_Main)
        loader = findViewById(R.id.loader)
        loader.visibility = View.GONE

        try {
            connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                Log.d(CAT, "upload all changes")
                uploadAllChange()
            }
        } catch (exception: Exception) {
            Toast.makeText(this@MainActivity, "No Internet connection", Toast.LENGTH_SHORT).show()
        }

        Log.d(CAT, "onCreate")
        btnLoginMain.setOnClickListener {
            if (edtUsername.text.toString() == "") {
                Toast.makeText(
                    this@MainActivity,
                    "please enter your username",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else if (edtPassword.text.toString() == "") {
                Toast.makeText(
                    this@MainActivity,
                    "please enter the password",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                login(edtUsername.text.toString(), edtPassword.text.toString())
            }
        }

        btnSigninMain.setOnClickListener {
            if (edtUsername.text.toString() == "") {
                Toast.makeText(this@MainActivity, "please enter your username", Toast.LENGTH_SHORT)
                    .show()
            } else if (edtPassword.text.toString() == "") {
                Toast.makeText(this@MainActivity, "please enter the password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                signin(edtUsername.text.toString(), edtPassword.text.toString(), token)
            }
        }

    }

    override fun onRestart() {
        super.onRestart()
        loader.visibility = View.GONE
        try {
            connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = connectivityManager.activeNetworkInfo
        } catch (exception: Exception) {
            Toast.makeText(this@MainActivity, "No Internet connection", Toast.LENGTH_SHORT).show()
        }

        try {
            connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null && networkInfo!!.isConnected) {
                Log.d(CAT, "upload all changes")
                uploadAllChange()
            }
        } catch (exception: Exception) {
            Toast.makeText(this@MainActivity, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    //the menu of preference setting realised by Action Bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d(CAT, "onCreateOptionMenu")
        menuInflater.inflate(R.menu.choix_menu, menu)
        return true
    }

    //Once the menu is clicked, jump to the SettingActivity interface
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(CAT, "onOptionsItemSelected")
        if (item.itemId == R.id.Preference) {
            val verPreference = Intent(this, SettingActivity::class.java)
            startActivity(verPreference)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private val mainActivityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private fun uploadAllChange() {
        mainActivityScope.launch {
            dataProvider.uploadAllChange()
        }
    }

    private fun login(username: String, password: String) {

        Log.d(CAT, "onConnection")

        //verify if the internet is connected
        if (networkInfo?.isConnected == false || networkInfo == null) {
            Toast.makeText(this@MainActivity, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
        mainActivityScope.launch {
            loader.visibility = View.VISIBLE

            Log.d(CAT, "onLogin")
            val user = getUser(username, password)
            editor.putString("token", user.hash)
            editor.apply()
            if (user.success) {
                Log.d(CAT, "login succeed")
                toChoixListActivity(username, user.hash)
            } else {
                Log.d(CAT, "login failed")
                loader.visibility = View.GONE
                Toast.makeText(
                    this@MainActivity,
                    "username or password is not correct",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun signin(username: String, password: String, token: String) {
        if (networkInfo?.isConnected == false) {
            Toast.makeText(this@MainActivity, "No Internet connection", Toast.LENGTH_SHORT).show()
        } else {
            mainActivityScope.launch {
                loader.visibility = View.VISIBLE

                var user = addUser(username, password, token)
                if (user.success) {
                    Toast.makeText(this@MainActivity, "New account created ", Toast.LENGTH_SHORT)
                        .show()
                    user = getUser(username,password)
                    toChoixListActivity(username, user.hash)
                } else {
                    loader.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "operation failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun getUser(username: String, password: String): UserResponse {
        return dataProvider.getUser(username, password) as UserResponse
    }

    private suspend fun addUser(username: String, password: String, token: String): UserResponse {
        return dataProvider.addUser(username, password, token)
    }

    private fun toChoixListActivity(username: String, token: String) {
        //jump to the ChoixListActivity interface when the login succeed
        val toChoixListActivity = Intent(this, ChoixListActivity::class.java)
        Log.d(CAT, "startJumping")
        toChoixListActivity.putExtra("url", url)
        toChoixListActivity.putExtra("token", token)
        toChoixListActivity.putExtra("username", username)
        startActivity(toChoixListActivity)
    }
}
