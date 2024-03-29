package ec.pmr.sequence1.ui

import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.EditTextPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.util.Log
import android.widget.Toast
import ec.pmr.sequence1.R

class SettingActivity : PreferenceActivity(), Preference.OnPreferenceChangeListener{
    private lateinit var editTextPreference:EditTextPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
        Log.d("SettingPref","starting")
        editTextPreference = findPreference("default_url") as EditTextPreference
        editTextPreference?.onPreferenceChangeListener = this

    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        Log.d("SettingPref","changing")
        Log.d("SettingPref",preference.key)
        if (preference.key == "default_login_pseudo") {
            Log.d("SettingPref",newValue.toString())
            Toast.makeText(this@SettingActivity, newValue.toString() + " restored", Toast.LENGTH_SHORT).show()
        }
        if (preference.key == "default_enabled") {
            Log.d("SettingPref",newValue.toString())
            if (newValue.toString() == "true") {
                Toast.makeText(this@SettingActivity, "default setting:On", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SettingActivity, "default setting:Off", Toast.LENGTH_SHORT).show()
            }
        }
        Log.d("SettingPref","changed")
        return true
    }
}



