package com.androdude.chatwho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androdude.chatwho.Mainscreen.Adapters.mAdapter
import com.androdude.chatwho.Mainscreen.MainActivity
import com.androdude.chatwho.Model_Class.Chat
import com.androdude.chatwho.Utils.UserOnlineStatus
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList


class ChatActivity : AppCompatActivity() {

    //Global Variable
    val uStatus = UserOnlineStatus()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        c.visibility= View.VISIBLE
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        back_icon.setOnClickListener()
        {
            onBackPressed()
        }

        //Firebase Variables
        val mUid = FirebaseAuth.getInstance().currentUser!!.uid
        val myRef = FirebaseDatabase.getInstance().getReference("messages")

        //Getting Receiver's Details
        val oUid = intent.getStringExtra("UID")
        val oUserName  = intent.getStringExtra("NAME")
        val oUserStatus = intent.getStringExtra("STATUS")
        val oUserImgURL = intent.getStringExtra("IMGURL")


        //Changing Letter To Uppercase
        val editedName = updatingName(oUserName)

        user_name_chat.setText(editedName)
        if(!(oUserImgURL.equals("default")))
        {
            Glide.with(this).load(oUserImgURL).into(user_profile_chat_image)
        }
        else
        {
            user_profile_chat_image.setBackgroundResource(R.drawable.ic_launcher_background)
        }



        //RecyclerView
        val cList = ArrayList<Chat>()
        val mAdapter = mAdapter(cList,this)
        chats_rView.adapter=mAdapter
        chats_rView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        sendMessages(myRef,mUid,oUid,mAdapter)

        readMessages(myRef,mUid,oUid,cList,mAdapter)

    }

    //Reading Messages
    private fun readMessages(myRef: DatabaseReference, mUid: String, oUid: String?, cList: ArrayList<Chat>, mAdapter: mAdapter) {
            myRef.child("chats").addValueEventListener(object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    cList.clear()
                    for(k in p0.children)
                    {

                        val objects = k.getValue(Chat::class.java)
                        if(objects != null)
                        {
                            if(objects!!.sender!!.equals(mUid) && objects.reciver!!.equals(oUid)
                                || objects.sender!!.equals(oUid) && objects.reciver!!.equals(mUid))
                            {
                                cList.add(objects)
                                SeenMessages(mUid,oUid)
                            }

                        }

                        mAdapter.notifyDataSetChanged()

                    }

                }

            })
    }


    //Sending Messages
    private fun sendMessages(myRef: DatabaseReference, mUid: String, oUid: String?, mAdapter: mAdapter) {
        send_bttn.setOnClickListener()
        {
            if(!(messages_edit_text.text.isEmpty()))
            {
                myRef.child("chats").push().setValue(Chat(messages_edit_text.text.toString(),mUid,oUid,"false"))
                chats_rView.smoothScrollToPosition(chats_rView.getBottom())
                mAdapter.notifyDataSetChanged()
                messages_edit_text.setText("")
            }
        }
    }

    //Check If Message Is Seen Or Not
    private fun SeenMessages(mUid : String?,oUid: String?)
    {
        val myRef = FirebaseDatabase.getInstance().getReference("messages").child("chats")

        myRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(k in p0.children)
                {
                    val objects = k.getValue(Chat::class.java)
                    if(objects!!.reciver.equals(FirebaseAuth.getInstance().currentUser!!.uid) && objects.sender.equals(oUid))
                    {
                        val hashMap = HashMap<String,Any>()
                        hashMap.put("seen","true")
                        k.ref.updateChildren(hashMap)
                    }
                }
            }

        })

    }



    //Updating First Letter To Caps
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


    override fun onResume() {
        super.onResume()
        uStatus.setOnline()
    }

    override fun onPause() {
        super.onPause()
        uStatus.setOffline()
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val infalter = menuInflater
        infalter.inflate(R.menu.user_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId.equals(R.id.delete_msg))
        {

            val myRef = FirebaseDatabase.getInstance().getReference("messages").child("chats").orderByChild("sender").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
            val myRef1 = FirebaseDatabase.getInstance().getReference("messages").child("chats").orderByChild("reciver").equalTo(FirebaseAuth.getInstance().currentUser!!.uid)

            myRef.addListenerForSingleValueEvent(object  : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(k in p0.children)
                    {
                        k.ref.removeValue()
                        Log.i("TAG","REMOVED")
                        startActivity(Intent(applicationContext,MainActivity::class.java))
                    }
                }

            })

            myRef1.addListenerForSingleValueEvent(object  : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for(k in p0.children)
                    {
                        k.ref.removeValue()
                        Log.i("TAG","REMOVED")
                        startActivity(Intent(applicationContext,MainActivity::class.java))
                    }
                }

            })
        }
        return super.onOptionsItemSelected(item)
    }





}
