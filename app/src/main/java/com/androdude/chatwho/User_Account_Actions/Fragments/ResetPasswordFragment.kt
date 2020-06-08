package com.androdude.chatwho.User_Account_Actions.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.androdude.chatwho.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_reset_password.view.*


/**
 * A simple [Fragment] subclass.
 */
class ResetPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val setupView = inflater.inflate(R.layout.fragment_reset_password, container, false)

        setupView.toolbar.setNavigationIcon(R.drawable.ic_back_button)
        setupView.toolbar.setNavigationOnClickListener()
        {
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container_layout,LoginFragment()).commit()
        }


        //Reset Password
        setupView.reset_password.setOnClickListener()
        {
            resetPassword(setupView)

        }



        return setupView
    }

    private fun resetPassword(setupView: View) {
        val mAuth = FirebaseAuth.getInstance()




        if(!(setupView.user_forget_account_email.text.isEmpty()))
        {
            setupView.user_forget_account_email.onEditorAction(EditorInfo.IME_ACTION_DONE)
            mAuth.sendPasswordResetEmail(setupView.user_forget_account_email.text.toString()).addOnCompleteListener()
            {
                if(it.isSuccessful)
                {
                    Snackbar.make(setupView,"Please Check Your Email For Next Steps",Snackbar.LENGTH_SHORT).show()
                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.container_layout,LoginFragment()).commit()
                }
                else
                {
                    Snackbar.make(setupView,"Error Email Address",Snackbar.LENGTH_SHORT).show()
                }
            }

        }


    }



}
