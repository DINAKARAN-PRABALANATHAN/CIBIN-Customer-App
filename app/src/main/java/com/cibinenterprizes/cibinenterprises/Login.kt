package com.cibinenterprizes.cibinenterprises

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        login_register.setOnClickListener{
            startActivity(Intent(this, Register::class.java))
            finish()
        }
        login_button.setOnClickListener {
            if(login_emailid.text.trim().toString().isNotEmpty() || login_password.text.trim().toString().isNotEmpty()){

                signInUser(login_emailid.text.trim().toString(), login_password.text.trim().toString())

            }else{
                Toast.makeText(this, "Input Required", Toast.LENGTH_LONG).show()
            }
        }
        login_forgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }
    }
    class NetworkTask(var activity: Login): AsyncTask<Void, Void, Void>(){
        var dialog = Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar)

        override fun onPreExecute() {
            val view = activity.layoutInflater.inflate(R.layout.full_screen_progress_bar,null)
            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.show()
            super.onPreExecute()
        }
        override fun doInBackground(vararg p0: Void?): Void? {
            Thread.sleep(1000)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            dialog.dismiss()
        }

    }
    private fun signInUser(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    if(auth.currentUser?.isEmailVerified!!){
                        NetworkTask(this).execute()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Email address is not verified. Please Verify your mail ID", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Email Id or Password is not Correct !! "+task.exception, Toast.LENGTH_LONG).show()
                }
            }
    }
}