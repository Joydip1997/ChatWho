package com.androdude.chatwho.Mainscreen.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.androdude.chatwho.Model_Class.Chat
import com.androdude.chatwho.R
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.left_msg_item.view.*

class mAdapter(val mList : ArrayList<Chat>,val context: Context) : RecyclerView.Adapter<mAdapter.mViewHolder>() {

    val RIGHT_MSG = 1
    val LEFT_MSG = 0

    val flag = false

    class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val t1 = itemView.findViewById<TextView>(R.id.msg)
        val t2 = itemView.findViewById<TextView>(R.id.is_seen_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
       if(viewType.equals(RIGHT_MSG))
       {
           return mViewHolder(LayoutInflater.from(context).inflate(R.layout.right_msg_item,null,false))
       }
        else
       {
           return mViewHolder(LayoutInflater.from(context).inflate(R.layout.left_msg_item,null,false))
       }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        holder.t1.setText(mList[position].msg)

        //Checking Is The The Message Is Seen By The User Or Not
        if(position == mList.size -1)
        {
            holder.t2.visibility=View.VISIBLE
            if(!(mList[position].isSeen!!.equals("false")))
            {
                holder.t2.setText("seen")
            }
            else
            {
                holder.t2.setText("delevired")
            }
        }
        else
        {
            holder.t2.visibility=View.GONE
        }

    }

    override fun getItemViewType(position: Int): Int {
        if(mList[position].sender!!.equals(FirebaseAuth.getInstance().currentUser!!.uid))
        {
            return RIGHT_MSG
            flag=true
        }
        else
        {
            return LEFT_MSG
            flag = false
        }
    }
}