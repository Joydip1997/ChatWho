package com.androdude.chatwho.User_Account_Actions

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androdude.chatwho.Mainscreen.MainActivity
import com.androdude.chatwho.R
import com.androdude.chatwho.User_Account_Actions.Fragments.LoginFragment
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        supportFragmentManager.beginTransaction().replace(R.id.container_layout,LoginFragment()).commit()
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null)
        {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}