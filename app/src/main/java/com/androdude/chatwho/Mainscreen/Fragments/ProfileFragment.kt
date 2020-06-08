package com.androdude.chatwho.Mainscreen.Fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.androdude.chatwho.Model_Class.User

import com.androdude.chatwho.R
import com.androdude.chatwho.User_Account_Actions.AccountActivity
import com.androdude.chatwho.Utils.LoadingClass
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.edit_details_dialog.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.user_edit_profile_image

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    //Variables
    var img_uri : Uri ?= null
    var mStorageReference = FirebaseStorage.getInstance().getReference("uploads")
    val myRef = FirebaseDatabase.getInstance().getReference("users")
    val mUid = FirebaseAuth.getInstance().currentUser!!.uid

    //Loading Animation
    val loadAnimation = LoadingClass()

    //Constants
    val PICK_IMAGE_REQUEST = 1000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val setupView = inflater.inflate(R.layout.fragment_profile, container, false)


        setupView.pToolbar.setNavigationIcon(R.drawable.ic_back_button)
        setupView.pToolbar.setNavigationOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.user_edit_container,SettingsFragment()).commit()
        }

        //showimage
        setupView.user_edit_profile_image.setOnClickListener()
        {
            val fragment = ImageFragment()
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.user_edit_container,fragment).commit()
        }




        //setImage
        setupView.update_image_button.setOnClickListener()
        {
            setimage(setupView)
        }

        //Editing User Details
        setupView.edit_user_name_button.setOnClickListener()
        {
            editDetails(activity!!,"Edit Your Name","name",setupView.edit_user_name.text.toString(),false,setupView)
        }

        setupView.edit_user_remark_button.setOnClickListener()
        {
            editDetails(activity!!,"Edit Your Status","user_remark",setupView.edit_user_status.text.toString(),false,setupView)
        }

        //Updating User Email
        setupView.edit_user_password_button.setOnClickListener()
        {
            editDetails(activity!!,"Edit Your Email Address","email",setupView.edit_user_email.text.toString(),true,setupView)
        }




        //Get Image
        getimage(setupView,activity!!)

        return setupView
    }

    private fun updateEmail(setupView: View)
    {
        val mAuth = FirebaseAuth.getInstance().currentUser
        setupView.edit_user_email.onEditorAction(EditorInfo.IME_ACTION_DONE)
        mAuth!!.updateEmail(setupView.edit_user_email.text.toString()).addOnCompleteListener()
        {
            if(it.isSuccessful)
            {
                val hashMap = HashMap<String,Any>()
                hashMap.put("email",setupView.edit_user_email.text.toString())
                Snackbar.make(setupView,"Email has been updated",Snackbar.LENGTH_SHORT).show()
            }
            else
            {
                Snackbar.make(setupView,"Error " + it.exception,Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    //Chose Image
    private fun setimage(setupView: View?) {

        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }


    //Upload Image
    private fun getFileExtension(uri: Uri): String? {
        val cR = activity!!.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile() {
        loadAnimation.get(activity!!)
        loadAnimation.run()
        if (img_uri != null) {
            val fileReference = mStorageReference.child(
                System.currentTimeMillis()
                    .toString() + "." + getFileExtension(img_uri!!)
            )
            val mUploadTask = fileReference.putFile(img_uri!!).continueWith {
                if (!it.isSuccessful) {

                    it.exception?.let { t ->
                        throw t
                    }
                }
                fileReference.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result!!.addOnSuccessListener { task ->
                        val myUri = task.toString()
                        val hashMap = HashMap<String, Any>()
                        hashMap.put("image_url", myUri)
                        myRef.child(mUid).updateChildren(hashMap)
                        loadAnimation.stop()
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == RESULT_OK)
        {
            if(data != null && data.data !== null)
            {
                img_uri=data.data
                uploadFile()
            }
            else
            {
                return
            }
        }
    }

    fun getimage(setupView: View?,context: Context)
    {
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
                    if(objects!!.user_id.equals(mUid))
                    {
                        if(!(objects!!.image_url.equals("default")))
                        {
                            Log.i("IMG",objects.image_url)
                           Glide.with(context).load(objects.image_url).into(setupView!!.user_edit_profile_image)
                        }


                        setupView!!.edit_user_name.setText(updatingName(objects.name!!))
                        setupView.edit_user_email.setText(updatingName(objects.email!!))

                        if(!(objects.user_remark.equals("")))
                        {
                            setupView.edit_user_status.setText(updatingName(objects.user_remark!!))
                        }
                        else
                        {
                            setupView.edit_user_email.setText("")
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


    fun editDetails(context: Context,text : String,data : String,details : String,flag : Boolean,setupView: View)
    {
        val myRef = FirebaseDatabase.getInstance().getReference("users")
        val mUid = FirebaseAuth.getInstance().currentUser!!.uid




        val alertDialog = AlertDialog.Builder(context)
        val mView = layoutInflater.inflate(R.layout.edit_details_dialog,null)
        mView.tView.setText(text)
        mView.eText.setText(details)



        alertDialog.setView(mView)
        val AlertDialog = alertDialog.create()

        mView.save_details_button.setOnClickListener()
        {
            if(!(mView.eText.text.isEmpty() || mView.eText.text.equals(details)))
            {
                val hashMap = HashMap<String,Any>()
                hashMap.put(data,mView.eText.text.toString())
                myRef.child(mUid).updateChildren(hashMap)

                if(flag)
                {
                    updateEmail(setupView)
                }

                AlertDialog.dismiss()
            }
        }

        mView.cancel_button.setOnClickListener()
        {
            AlertDialog.dismiss()
        }




            AlertDialog.show()




}



}
