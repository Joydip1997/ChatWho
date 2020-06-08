package com.androdude.chatwho.Mainscreen.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androdude.chatwho.Model_Class.Chat
import com.androdude.chatwho.Model_Class.User
import com.androdude.chatwho.R
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class cAdapter(val mList : ArrayList<User>, val context: Context,val mUid: String?) : RecyclerView.Adapter<cAdapter.mViewHolder>() {

    lateinit var mListener : onItemClickListener

    interface onItemClickListener
    {
        fun getPos(pos : Int)
    }

    fun setOnItemClickListener(listener: cAdapter.onItemClickListener)
    {
        mListener=listener
    }



    class mViewHolder(itemView: View,mListener : onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val t1 = itemView.findViewById<CircleImageView>(R.id.last_user_profile_image)
        val t2 = itemView.findViewById<CircleImageView>(R.id.is_online)
        val t3 = itemView.findViewById<TextView>(R.id.last_user_name)
        val t4 = itemView.findViewById<TextView>(R.id.last_user_message)
        val t5 = itemView.findViewById<TextView>(R.id.number_of_messages)

        init {
            itemView.setOnClickListener()
            {
                if(mListener != null && adapterPosition != RecyclerView.NO_POSITION)
                {
                    mListener.getPos(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {

        return mViewHolder(LayoutInflater.from(context).inflate(R.layout.last_msg_layout, null, false),mListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {

        if (!(mList[position].image_url.equals("default"))) {
            Glide.with(context).load(mList[position].image_url).into(holder.t1)
        } else {
            holder.t1.setBackgroundResource(R.drawable.ic_launcher_background)
        }

        if (!(mList[position].status.equals("offline"))) {
            holder.t2.visibility = View.VISIBLE
        } else {
            holder.t2.visibility = View.GONE
        }

        holder.t3.setText(mList[position].name)

        lastMessage(mList[position].user_id.toString(),holder.t4,holder.t5,mUid)
    }

    fun lastMessage(userid : String,tView : TextView,tView1 : TextView,mUid : String?) {

        var lastMsg = "default"
        var i = 0
        val myRef = FirebaseDatabase.getInstance().getReference("messages").child("chats")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (k in p0.children) {
                    val chats = k.getValue(Chat::class.java)
                    if (chats!!.reciver.equals(mUid) && chats.sender.equals(userid)
                        || chats.sender.equals(mUid) && chats.reciver.equals(userid)) {

                        lastMsg = chats.msg!!
                    }
                    if(chats.reciver.equals(mUid) && chats.sender.equals(userid))
                    {
                        if(chats.isSeen.equals("false"))
                        {
                            i++
                        }
                    }
                }
                if(i==0)
                {
                    tView1.visibility=View.GONE
                }
                else
                {
                    tView1.setText(i.toString())
                    tView1.visibility=View.VISIBLE
                }
                tView.setText(lastMsg)

            }

        })
    }


}

