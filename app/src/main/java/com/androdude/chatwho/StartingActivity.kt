package com.androdude.chatwho

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.androdude.chatwho.Mainscreen.MainActivity
import com.androdude.chatwho.Mainscreen.ProfileActivity
import com.androdude.chatwho.User_Account_Actions.AccountActivity
import com.google.firebase.auth.FirebaseAuth

class StartingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)

        val handler = Handler()

        val r: Runnable = object : Runnable {
            override fun run() {
                if(FirebaseAuth.getInstance().currentUser != null)
                {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                }
                else {
                    startActivity(Intent(applicationContext, AccountActivity::class.java))
                }

            }
        }

        handler.postDelayed(r, 3000)
    }
}
