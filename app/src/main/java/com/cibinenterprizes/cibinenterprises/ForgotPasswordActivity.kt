package com.cibinenterprizes.cibinenterprises

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()
        forgot_password_button.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
            if(forgot_password_email_id.text.toString().isEmpty()){
                forgot_password_message.text = "Email Address is not provided"
            }else{
                auth.sendPasswordResetEmail(forgot_password_email_id.text.toString()).addOnCompleteListener(this) {
                    if(it.isSuccessful){
                        forgot_password_message.text = "Rest Password Link is Mailed"
                    }else{
                        forgot_password_message.text = "Password Reset mail could not be send"
                    }
                }
            }
        }
        forgot_password_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}