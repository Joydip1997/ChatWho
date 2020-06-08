package com.androdude.chatwho.User_Account_Actions.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androdude.chatwho.Mainscreen.MainActivity

import com.androdude.chatwho.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val setupView = inflater.inflate(R.layout.fragment_login, container, false)

        //Firebase Variables
        val mAuth = FirebaseAuth.getInstance()
        val myRef = FirebaseDatabase.getInstance().getReference("users")

        //Opening Register Fragment
        openingRegisterFragment(setupView)

        //Opening Reset Password Fragment
        openingResetPasswordFragment(setupView)

        //Logging In User
        loginUser(setupView,mAuth,myRef)

        return setupView
    }

    private fun openingResetPasswordFragment(setupView: View) {

        setupView.forget_password_textview.setOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container_layout,ResetPasswordFragment()).commit()
        }

    }

    private fun loginUser(setupView: View, mAuth: FirebaseAuth, myRef: DatabaseReference) {
       setupView.login_button.setOnClickListener()
       {
           if(!(setupView.user_login_email.text.isEmpty() || setupView.user_login_password.text.isEmpty()))
           {
               mAuth.signInWithEmailAndPassword(setupView.user_login_email.text.toString(),setupView.user_login_password.text.toString())
                   .addOnCompleteListener()
                   {
                       if(it.isSuccessful)
                       {
                           startActivity(Intent(activity,MainActivity::class.java))
                       }
                       else
                       {
                           Toast.makeText(context,"Error " + it.exception,Toast.LENGTH_SHORT).show()
                       }
                   }
           }
       }

    }

    private fun openingRegisterFragment(upView: View) {
        upView.register_fragment.setOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container_layout, RegisterFragment()).commit()
        }
    }

}
