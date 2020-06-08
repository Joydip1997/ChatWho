package com.androdude.chatwho.Utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.androdude.chatwho.Mainscreen.Adapters.uAdapter
import com.androdude.chatwho.Model_Class.User
import com.androdude.chatwho.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_image.view.*

class ImageClass {



  fun showDetails(context: Context,image : ImageView,text : TextView) {

        val myRef = FirebaseDatabase.getInstance().getReference("users")
        val mUid = FirebaseAuth.getInstance().currentUser!!.uid

        myRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(k in p0.children)
                {
                    val objects = k.getValue(User::class.java)
                    if(objects!!.user_id!!.equals(mUid))
                    {
                        if(objects.image_url.equals("default"))
                        {
                            image.setBackgroundResource(R.drawable.ic_launcher_background)
                        }
                        else
                        {
                            Glide.with(context).load(objects.image_url).into(image.show_user_image)
                        }
                        text.setText(updatingName(objects.name!!))
                    }
                }
            }

        })


    }

    fun updatingName(name : String) : String
    {
        var nName = ""
        var i = 1
        while(i<name.length)
        {
            nName += name[i]
            i++
        }
        return name.get(0).toString().toUpperCase() + nName
    }



}