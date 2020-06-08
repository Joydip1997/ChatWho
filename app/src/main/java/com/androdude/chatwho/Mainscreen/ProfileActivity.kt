package com.androdude.chatwho.Mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.androdude.chatwho.Mainscreen.Fragments.ProfileFragment
import com.androdude.chatwho.Mainscreen.Fragments.SettingsFragment
import com.androdude.chatwho.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)




        //Opening Profile Edit Fragment
        supportFragmentManager.beginTransaction().replace(R.id.user_edit_container,SettingsFragment()).commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
