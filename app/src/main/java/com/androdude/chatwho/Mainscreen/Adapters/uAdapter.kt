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
import com.androdude.chatwho.Utils.ImageClass
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.contact_user_list_layout.view.*

class uAdapter(val mList : ArrayList<User>,val context: Context) : RecyclerView.Adapter<uAdapter.mViewHolder>()
{

    lateinit var mListener: onItemClickListener

    val imageClass = ImageClass()

    interface onItemClickListener
    {
        fun getpos(pos : Int)
    }


    fun setOnItemClickListener(listener: onItemClickListener)
    {
        mListener=listener
    }


    class mViewHolder(itemview : View,mListener : onItemClickListener) : RecyclerView.ViewHolder(itemview)
    {
        val t1 = itemview.findViewById<CircleImageView>(R.id.user_list_profile_image)
        val t2 = itemview.findViewById<CircleImageView>(R.id.user_list_online_image)
        val t3 = itemview.findViewById<TextView>(R.id.user_list_name)

        init {
            itemview.setOnClickListener()
            {

                if(mListener != null && adapterPosition != RecyclerView.NO_POSITION)
                {
                    mListener.getpos(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {

            return mViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_user_list_layout,null,false),mListener)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
       holder.t3.setText(mList[position].name)

        //Checking Online Or Offline
        if(mList[position].status!!.equals("online"))
        {
            holder.t2.visibility=View.VISIBLE
        }
        else
        {
            holder.t2.visibility=View.GONE
        }

        if(!(mList[position].image_url.equals("default")))
        {
            Glide.with(context).load(mList[position].image_url).into(holder.t1)
        }
        else
        {
            holder.t1.setBackgroundResource(R.drawable.ic_launcher_background)
        }




    }



}