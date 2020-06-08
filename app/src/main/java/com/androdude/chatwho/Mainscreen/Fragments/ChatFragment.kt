package com.androdude.chatwho.Mainscreen.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.androdude.chatwho.ChatActivity
import com.androdude.chatwho.Mainscreen.Adapters.cAdapter
import com.androdude.chatwho.Mainscreen.Adapters.uAdapter
import com.androdude.chatwho.Model_Class.Chat
import com.androdude.chatwho.Model_Class.User

import com.androdude.chatwho.R
import com.androdude.chatwho.Utils.LoadingClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.util.stream.Collectors


class ChatFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val setupView =  inflater.inflate(R.layout.fragment_chat, container, false)

        //RecyclerView
        val mList = ArrayList<User>()
        val Adapter = cAdapter(mList,activity!!,FirebaseAuth.getInstance().currentUser!!.uid)
        setupView.chat_rView.adapter=Adapter
        setupView.chat_rView.layoutManager=LinearLayoutManager(activity)

        setLastImages(setupView,mList,Adapter)

        Adapter.setOnItemClickListener(object : cAdapter.onItemClickListener
        {
            override fun getPos(pos: Int) {
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("UID",mList[pos].user_id)
                intent.putExtra("NAME",mList[pos].name)
                intent.putExtra("STATUS",mList[pos].status)
                intent.putExtra("IMGURL",mList[pos].image_url)
                startActivity(intent)
            }
        })



        return setupView
    }

    private fun setLastImages(setupView: View?,mList : ArrayList<User>,mAdapter: cAdapter) {

        val mUid = FirebaseAuth.getInstance().currentUser!!.uid
        val myRef = FirebaseDatabase.getInstance().getReference("messages").child("chats")
        var uIds_List = ArrayList<String>()

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                uIds_List.clear()
               for(k in p0.children)
               {
                   val objects = k.getValue(Chat::class.java)
                   if(objects!!.sender.equals(mUid))
                   {
                       uIds_List.add(objects.reciver.toString())
                   }
                   if(objects.reciver.equals(mUid))
                   {
                       uIds_List.add(objects.sender.toString())
                   }
               }
               val newList =  uIds_List.stream().distinct().collect(Collectors.toList()) as ArrayList
                getusers(newList,mList,mAdapter,activity!!)

                var i = 0
                while(i<newList.size)
                {
                    Log.i("TAG",newList[i])
                    i++
                }

            }

        })



    }

    fun getusers(oIds : ArrayList<String>,mList : ArrayList<User>,mAdapter: cAdapter,context: Context)
    {
        val mUid = FirebaseAuth.getInstance().currentUser!!.uid
        val myRef = FirebaseDatabase.getInstance().getReference("users")

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var i = 0
                mList.clear()
                for(c in p0.children)
                {

                    val user = c.getValue(User::class.java)
                    for(ids in oIds)
                    {
                        if(user!!.user_id.equals(ids))
                        {
                            mList.add(user)
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

        })
    }


}
