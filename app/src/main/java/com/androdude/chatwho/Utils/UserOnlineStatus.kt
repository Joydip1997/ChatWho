package com.androdude.chatwho.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserOnlineStatus {

    val mAuth = FirebaseAuth.getInstance().currentUser!!.uid
    val myRef = FirebaseDatabase.getInstance().getReference("users")

    fun setOnline()
    {
        val hashMap = HashMap<String,Any>()
        hashMap.put("status","online")
        myRef.child(mAuth).updateChildren(hashMap)
    }

    fun setOffline()
    {
        val hashMap = HashMap<String,Any>()
        hashMap.put("status","offline")
        myRef.child(mAuth).updateChildren(hashMap)
    }
}