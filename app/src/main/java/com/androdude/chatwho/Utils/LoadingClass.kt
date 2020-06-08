package com.androdude.chatwho.Utils

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import com.androdude.chatwho.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_screen.view.*


 class LoadingClass {

     private var  progresDialog: ProgressDialog? = null
     private var context:Context? =null



     fun get(Context: Context)
     {

         context=Context

     }
     fun run()
     {
         progresDialog= ProgressDialog(context)
         progresDialog!!.show()





         progresDialog!!.setContentView(R.layout.loading_screen1)


//

         progresDialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
     }

     fun stop()
     {
         progresDialog!!.hide()

     }

}