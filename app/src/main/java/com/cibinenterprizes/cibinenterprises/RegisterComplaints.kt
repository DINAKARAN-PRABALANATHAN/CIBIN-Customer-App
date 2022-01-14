package com.cibinenterprizes.cibinenterprises

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cibinenterprizes.cibinenterprises.Model.BinExtra
import com.cibinenterprizes.cibinenterprises.Model.ComplainDetails
import com.cibinenterprizes.cibinenterprises.Model.FCMResponse
import com.cibinenterprizes.cibinenterprises.Model.FCMSendData
import com.cibinenterprizes.cibinenterprises.Remote.IFCMService
import com.cibinenterprizes.cibinenterprises.Remote.RetrofitFCMClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register_complaints.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterComplaints : AppCompatActivity() {

    val idAuth = FirebaseAuth.getInstance().currentUser
    val databaseReference= FirebaseDatabase.getInstance().reference
    lateinit var ifcmService: IFCMService
    lateinit var district: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_complaints)

        register_complaints_email.setText(idAuth?.email)

        ifcmService = RetrofitFCMClient.getInstance("https://fcm.googleapis.com/")
            .create(IFCMService::class.java)


        register_complaints_back_botton.setOnClickListener {
            finish()
        }
        var getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                district = snapshot.child("User Details").child(idAuth?.uid.toString()).child("Profile").child("district").getValue().toString()
                val complaintCount = snapshot.child("User Details").child("COMPLAINT ID").getValue().toString()
                val complaintId = complaintCount.toInt()
                var id = complaintId + 1
                register_complaints_botton.setOnClickListener{
                    val area = register_complaints_area.text.toString()
                    val binId = register_complaints_bin_id.text.toString()
                    val complaint = register_complaints_complaints.text.toString()
                    val status = "Pending"
                    val complaintDetail = ComplainDetails(idAuth?.email,binId,area,complaint,status,id.toString())
                    val userName = snapshot.child("User Details").child(idAuth?.uid.toString()).child("Profile").child("username").getValue()
                    val emailId = snapshot.child("User Details").child(idAuth?.uid.toString()).child("Profile").child("emailId").getValue()
                    val mobile = snapshot.child("User Details").child(idAuth?.uid.toString()).child("Profile").child("mobile").getValue()
                    val binExtra = BinExtra(userName as String?, emailId as String?, mobile as String?,idAuth?.uid.toString())
                    databaseReference.child("User Details").child(idAuth?.uid.toString()).child("Complaints").child(id.toString()).setValue(complaintDetail).addOnCompleteListener {
                        databaseReference.child("User Details").child("COMPLAINT ID").setValue(id).addOnCompleteListener{
                            databaseReference.child("Complaints").child(district).child(id.toString()).setValue(complaintDetail).addOnCompleteListener {
                                databaseReference.child("Complaints").child(district).child(id.toString()).child("UserDetails").setValue(binExtra).addOnCompleteListener {
                                    databaseReference.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val adminToken = snapshot.child("AdminToken").child(district).getValue().toString()
                                            val dataSend = HashMap<String, String>()
                                            dataSend.put("title", "Bin Created")
                                            dataSend.put(
                                                "content",
                                                "New Bin is created. BIN ID ${id.toString()} verify it soon..."
                                            )
                                            val sendData = FCMSendData(
                                                adminToken,
                                                dataSend
                                            )
                                            ifcmService.sendNotification(sendData)
                                                .enqueue(object : Callback<FCMResponse?> {

                                                    override fun onResponse(call: Call<FCMResponse?>, response: Response<FCMResponse?>) {
                                                        if (response.code() == 200) {
                                                            if (response.body()!!.success != 1) {
                                                                Toast.makeText(this@RegisterComplaints, "Failed ", Toast.LENGTH_LONG).show()
                                                                finish()
                                                            }else
                                                                finish()
                                                        }
                                                    }

                                                    override fun onFailure(call: Call<FCMResponse?>, t: Throwable?) {
                                                        finish()
                                                    }
                                                })
                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }

                                    })
                                    Toast.makeText(this@RegisterComplaints,"Complain Registered successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReference.addValueEventListener(getData)
        databaseReference.addListenerForSingleValueEvent(getData)
    }
}