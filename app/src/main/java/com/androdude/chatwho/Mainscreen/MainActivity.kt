package com.androdude.chatwho.Mainscreen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.androdude.chatwho.Mainscreen.Adapters.pageAdapter
import com.androdude.chatwho.R
import com.androdude.chatwho.User_Account_Actions.AccountActivity
import com.androdude.chatwho.Utils.LoadingClass
import com.androdude.chatwho.Utils.UserOnlineStatus
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_screen.view.*


class MainActivity : AppCompatActivity() {

    //Global Variable
    val uStatus = UserOnlineStatus()

    //Loading Animation Class
    val loadAnimation = LoadingClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        checkNetworkState()

        val toolbar = findViewById<Toolbar>(R.id.toolbar1)
        setSupportActionBar(toolbar)


        val pAdapter = pageAdapter(supportFragmentManager,tab_layout.tabCount)

        viewPager.adapter=pAdapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))


        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                viewPager.currentItem=p0!!.position
                if(p0.position == 0)
                {
                    pAdapter.notifyDataSetChanged()
                }
                else if( p0.position == 1)
                {
                    pAdapter.notifyDataSetChanged()
                }
                else
                {
                    pAdapter.notifyDataSetChanged()
                }

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
               viewPager.currentItem=p0!!.position
                if(p0.position == 0)
                {
                    pAdapter.notifyDataSetChanged()
                }
                else if( p0.position == 1)
                {
                    pAdapter.notifyDataSetChanged()
                }
                else
                {
                    pAdapter.notifyDataSetChanged()
                }
            }

        })


    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val infalter = menuInflater
        infalter.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.Logout)
        {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,AccountActivity::class.java))
        }
        if(item.itemId == R.id.Settings)
        {
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        uStatus.setOnline()
    }

    override fun onPause() {
        super.onPause()
        uStatus.setOffline()
    }

    fun checkNetworkState()
    {
        val conMgr =  getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            Log.i("Tag","ONLINE")

        } else {
            Log.i("Tag","OFFLINE")
            run(this)

        }
    }

    fun run(context : Context) : Boolean
    {

        val alertDialog = AlertDialog.Builder(context)
        val mView = LayoutInflater.from(context).inflate(R.layout.loading_screen,null)
        var flag = false

        main_activity_relative_layout.visibility=View.GONE

        alertDialog.setView(mView)
        val AlertDialog = alertDialog.create()

        mView.try_again_button.setOnClickListener()
        {

            AlertDialog.dismiss()
            main_activity_relative_layout.visibility=View.VISIBLE
            startActivity(Intent(this,MainActivity::class.java))
        }


        AlertDialog.show()

        return flag
    }




}
