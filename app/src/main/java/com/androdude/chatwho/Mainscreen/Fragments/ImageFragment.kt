package com.androdude.chatwho.Mainscreen.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.androdude.chatwho.Model_Class.User

import com.androdude.chatwho.R
import com.androdude.chatwho.Utils.ImageClass
import com.androdude.chatwho.Utils.LoadingClass
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import kotlinx.android.synthetic.main.fragment_image.view.show_user_image

/**
 * A simple [Fragment] subclass.
 */
class ImageFragment : Fragment() {

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

        val setupView =  inflater.inflate(R.layout.fragment_image, container, false)


        setupView.image_Toolbar.setNavigationIcon(R.drawable.ic_back_button)
        setupView.image_Toolbar.setNavigationOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.user_edit_container,ProfileFragment()).commit()
        }


        //Seting Image and Email Address
        val imageClass = ImageClass()
        imageClass.showDetails(activity!!,setupView.show_user_image,setupView.show_user_email_address)



        setupView.edit_image.setOnClickListener()
        {
            setimage()
        }


        return setupView
    }



    private fun setimage() {

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

        if(requestCode==PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK)
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





}
