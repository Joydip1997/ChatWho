package com.androdude.chatwho.Mainscreen.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androdude.chatwho.Mainscreen.MainActivity
import com.androdude.chatwho.Mainscreen.ProfileActivity
import com.androdude.chatwho.Model_Class.User

import com.androdude.chatwho.R
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_seetings.view.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val setupView = inflater.inflate(R.layout.fragment_seetings, container, false)

        setupView.sToolbar.setNavigationIcon(R.drawable.ic_back_button)
        setupView.sToolbar.setNavigationOnClickListener()
        {
            startActivity(Intent(activity!!,MainActivity::class.java))
        }


        setupView.linear_layout4.setOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.user_edit_container,ProfileFragment()).commit()
        }


        //Getting User Personal Details Phase 1
        getDetails(setupView,activity!!)

        //Remove User Account
        setupView.inside_linear_layout1.setOnClickListener()
        {
           // removeAccount(activity!!)
        }


        return setupView
    }

    private fun removeAccount(context: Context) {
        val mUID = FirebaseAuth.getInstance().currentUser!!.uid
        val mAuth = FirebaseAuth.getInstance()
        val mRef = FirebaseDatabase.getInstance().getReference("users").child(mUID)

        val user = FirebaseAuth.getInstance().currentUser!!

        mAuth.removeAuthStateListener {  }

        user.delete().addOnCompleteListener()
        {
            if(it.isSuccessful)
            {

                mRef.removeValue().addOnCompleteListener()
                {
                    FirebaseAuth.getInstance().signOut()
                    if(it.isSuccessful)
                    {
                        startActivity(Intent(context,ProfileActivity::class.java))
                    }
                }
            }
            else
            {
                Toast.makeText(context,"Error " +  it.exception, Toast.LENGTH_SHORT).show()
            }
        }




    }

    fun getDetails(setupView: View?,context: Context)
    {
        val mUid = FirebaseAuth.getInstance().currentUser!!.uid
        val myRef = FirebaseDatabase.getInstance().getReference("users")

        myRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {
                for(k in p0.children)
                {
                    val objects = k.getValue(User::class.java)
                    if(objects!!.user_id.equals(mUid))
                    {
                        if(!(objects.image_url.equals("default")))
                        {
                            Glide.with(context).load(objects.image_url).into(setupView!!.user_profle_image)
                        }
                        else
                        {
                            setupView!!.user_profle_image.setBackgroundResource(R.drawable.ic_launcher_background)
                        }

                        setupView.user_name.setText(updatingName(objects.name!!))

                        if(!(objects.user_remark.equals("")))
                        {
                            setupView.user_remark.setText(updatingName(objects.user_remark!!))
                        }
                        else
                        {
                            setupView.user_remark.setText("")
                        }
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
