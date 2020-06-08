package com.androdude.chatwho.User_Account_Actions.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.androdude.chatwho.Mainscreen.MainActivity
import com.androdude.chatwho.Model_Class.User

import com.androdude.chatwho.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val setupView = inflater.inflate(R.layout.fragment_register, container, false)

        //Firebase Variables
        val mAuth = FirebaseAuth.getInstance()
        val myRef = FirebaseDatabase.getInstance().getReference("users")

        //Opening Register Fragment
        openingregisterFragment(setupView)

        //Registering New User
        registerUser(setupView,mAuth,myRef)


        return setupView

    }

    private fun registerUser(setupView: View, mAuth: FirebaseAuth, myRef: DatabaseReference) {
        setupView.register_button.setOnClickListener()
        {
            if(!(setupView.user_reg_name.text.isEmpty() || setupView.user_reg_email_address.text.isEmpty()
                        || setupView.user_reg_password.text.isEmpty()))
            {
                mAuth.createUserWithEmailAndPassword(setupView.user_reg_email_address.text.toString(),setupView.user_reg_password.text.toString())
                    .addOnCompleteListener()
                    {
                        if(it.isSuccessful)
                        {
                            myRef.child(mAuth.currentUser!!.uid).setValue(User(mAuth.currentUser!!.uid,setupView.user_reg_name.text.toString(),
                                setupView.user_reg_email_address.text.toString()
                            ,"offline","default","Available"))
                            startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }
            }
        }

    }


    private fun openingregisterFragment(upView: View) {
        upView.login_fragment.setOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container_layout,LoginFragment()).commit()
        }

    }




}
