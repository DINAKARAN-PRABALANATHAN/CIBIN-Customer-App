package com.cibinenterprizes.cibinenterprises

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.cibinenterprizes.cibinenterprises.Model.UserProfile
import com.google.android.gms.common.wrappers.Wrappers.packageManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var TAKE_IMAGE_CODE = 10001
    lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        val profilePhoto = findViewById<CircleImageView>(R.id.registration_profile)

        registration_login.setOnClickListener{
            startActivity(Intent(this,Login::class.java))
        }
        registration_profile.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, TAKE_IMAGE_CODE)
            }
        }
        registration_button.setOnClickListener {
            profilePhoto.setImageResource(R.drawable.waring)
            return@setOnClickListener
        }
        register_terms_and_conditions_view.setOnClickListener {
            startActivity(Intent(this,TermsAndConditions::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMAGE_CODE){
            when(resultCode){
                RESULT_OK -> bitMap(data)
            }
        }
    }

    private fun bitMap(data: Intent?) {
        val bitmap = data?.getExtras()?.get("data")
        registration_profile.setImageBitmap(bitmap as Bitmap?)
        registration_button.setOnClickListener {
            if(registration_email_id.text.trim().toString().isNotEmpty() || registration_password.text.trim().toString().isNotEmpty() || registration_user_name.text.trim().toString().isNotEmpty() || registration_mobile_number.text.trim().toString().isNotEmpty()){
                if(registration_password.text.trim().toString() == registration_conform_password.text.trim().toString()){
                    if (register_terms_and_conditions.isChecked){
                        progressDialog = ProgressDialog(this)

                        val spinner = Spinner(this)
                        val districts = arrayOf("Choose a district", "Ariyalur", "Chengalpattu", "Chennai", "Coimbatore", "Cuddalore", "Dharmapuri", "Dindigul", "Erode", "Kallakurichi", "Kanchipuram", "Kanyakumari", "Karur", "Krishnagiri", "Madurai", "Mayiladuthurai", "Nagapattinam", "Namakkal", "Nilgiris", "Perambalur", "Pudukkottai", "Ramanathapuram", "Ranipet", "Salem", "Sivagangai", "Tenkasi", "Thanjavur", "Theni", "Thoothukudi", "Tiruchirappalli", "Tirunelveli", "Tirupattur", "Tiruppur","Tiruvallur", "Tiruvannamalai", "Tiruvarur", "Vellore", "Viluppuram", "Virudhunagar")
                        val arrayAdapter = ArrayAdapter(this@Register, android.R.layout.simple_spinner_item, districts)
                        spinner.adapter = arrayAdapter
                        val alertDialogBuilder = AlertDialog.Builder(this)
                        alertDialogBuilder.setTitle("Choose a district")
                        alertDialogBuilder.setView(spinner)
                        alertDialogBuilder.setPositiveButton("Ok", null)
                        val alertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                            val district = spinner.selectedItem.toString()
                            alertDialog.dismiss()
                            if (district == "Choose a district"){
                                Toast.makeText(this, "Please choose a district",Toast.LENGTH_LONG).show()
                            }else{
                                progressDialog.show()
                                progressDialog.setContentView(R.layout.full_screen_progress_bar)
                                progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                progressDialog.setCancelable(false)

                                auth.createUserWithEmailAndPassword(registration_email_id.text.trim().toString(),registration_password.text.trim().toString()).addOnCompleteListener(this) { task ->
                                    if(task.isSuccessful){

                                        val user = auth.currentUser
                                        user?.sendEmailVerification()?.addOnCompleteListener {
                                            if (it.isSuccessful){
                                                Toast.makeText(this, "Registration Successful\nVerification mail send to your mail",Toast.LENGTH_LONG).show()
                                                handleUpload(bitmap, district)
                                            }
                                        }
                                    }else{
                                        Toast.makeText(this, "User Creation is Failed...",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                        }
                    }else{
                        Toast.makeText(this, "Please accept the Terms and conditions",Toast.LENGTH_LONG).show()
                    }
                }else{
                    registration_conform_password.error = "Password need to be same"
                    registration_conform_password.requestFocus()
                    return@setOnClickListener
                }
            }else{
                Toast.makeText(this, "Input Reqired", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap?, district: String) {
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG,100, baos)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val firebaseStorage = FirebaseStorage.getInstance().reference.child(uid+".jpeg")
        firebaseStorage.putBytes(baos.toByteArray()).addOnSuccessListener {
            getDownloadUrl(firebaseStorage, district)
        }.addOnFailureListener {
            Toast.makeText(this, "Firebase Storage is not done", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDownloadUrl(firebaseStorage: StorageReference, district: String) {
        firebaseStorage.downloadUrl.addOnSuccessListener {
            setUserProfileUrl(it, district)
        }
    }

    private fun setUserProfileUrl(it: Uri?, district: String) {
        val url = it.toString().substring(0,it.toString().indexOf("&token"))
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder().setPhotoUri(it).build()
        user?.updateProfile(request)?.addOnSuccessListener {
            val idAuth = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val db = FirebaseDatabase.getInstance().reference
            val userProfile = UserProfile(registration_user_name.text.trim().toString(), registration_email_id.text.trim().toString(), registration_mobile_number.text.trim().toString(), url, district)
            db.child("User Details").child(idAuth).child("Profile").setValue(userProfile).addOnCompleteListener {
                db.child("User Details").child(idAuth).child("Profile").child("TermsAndConditions").setValue("Accepted").addOnCompleteListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                }
            }
        }?.addOnFailureListener {
            Toast.makeText(this, "Profile image failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun showDialogSpinner() {

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser

        if(user != null){
            if(auth.currentUser?.isEmailVerified!!){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Email address is not verified. Please Verify your mail ID", Toast.LENGTH_LONG).show()
            }
        }
    }

}
