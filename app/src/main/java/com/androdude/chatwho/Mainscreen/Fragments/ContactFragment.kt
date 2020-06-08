package com.androdude.chatwho.Mainscreen.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androdude.chatwho.ChatActivity
import com.androdude.chatwho.Mainscreen.Adapters.uAdapter
import com.androdude.chatwho.Model_Class.User

import com.androdude.chatwho.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_contact.view.*

/**
 * A simple [Fragment] subclass.
 */
class ContactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val setupView = inflater.inflate(R.layout.fragment_contact, container, false)


        //Firebase Variables
        val mUID = FirebaseAuth.getInstance().currentUser!!.uid
        val myRef = FirebaseDatabase.getInstance().getReference("users")


       showContacts(mUID,myRef,setupView,activity!!)


        return setupView
    }

    private fun showContacts( mUID: String, myRef: DatabaseReference, setupView: View,activity: FragmentActivity) {

        val uList = ArrayList<User>()



        val mAdapter = uAdapter(uList,activity)


        setupView.contact_user_rView.adapter=mAdapter
        setupView.contact_user_rView.layoutManager=LinearLayoutManager(activity)


        //Getting Contact List
        myRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                uList.clear()
               for(k in p0.children)
              {
                  val objects = k.getValue(User::class.java)
                  if(!(objects!!.user_id!!.equals(mUID)))
                  {
                      uList.add(objects)
                  }
                  mAdapter.notifyDataSetChanged()
              }
            }

        })

        //Going To Chat Activity
        mAdapter.setOnItemClickListener(object : uAdapter.onItemClickListener
        {
            override fun getpos(pos: Int) {
                val intent = Intent(activity,ChatActivity::class.java)
                intent.putExtra("UID",uList[pos].user_id)
                intent.putExtra("NAME",uList[pos].name)
                intent.putExtra("STATUS",uList[pos].status)
                intent.putExtra("IMGURL",uList[pos].image_url)
                startActivity(intent)
            }

        })

    }


}
