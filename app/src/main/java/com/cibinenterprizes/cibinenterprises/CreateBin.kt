package com.cibinenterprizes.cibinenterprises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.cibinenterprizes.cibinenterprises.Model.BinDetails
import com.cibinenterprizes.cibinenterprises.Model.BinExtra
import com.cibinenterprizes.cibinenterprises.Model.FCMResponse
import com.cibinenterprizes.cibinenterprises.Model.FCMSendData
import com.cibinenterprizes.cibinenterprises.Remote.IFCMService
import com.cibinenterprizes.cibinenterprises.Remote.RetrofitFCMClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_create_bin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBin : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val databaseReference= FirebaseDatabase.getInstance().reference
    lateinit var spinner1: Spinner
    lateinit var spinner2: Spinner
    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val idAuth = FirebaseAuth.getInstance().currentUser?.uid.toString()
    lateinit var ifcmService: IFCMService
    lateinit var adminToken: String
    lateinit var district: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bin)

        ifcmService = RetrofitFCMClient.getInstance("https://fcm.googleapis.com/")
            .create(IFCMService::class.java)



        var getData = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val binCount = snapshot.child("User Details").child("BinCount").getValue().toString()
                val binId = binCount.toInt()
                var id = binId+1
                create_bin_botton.setOnClickListener{
                    val area_village = create_bin_area.text.toString()
                    val locality = create_bin_locality.text.toString()
                    val loadType = spinner1.getSelectedItem().toString()
                    val collectionPeriod = spinner2.getSelectedItem().toString()
                    val lantitude = create_bin_lantitude.text.toString()
                    val longitude = create_bin_longitude.text.toString()
                    district = snapshot.child("User Details").child(user?.uid.toString()).child("Profile").child("district").getValue().toString()
                    adminToken = snapshot.child("AdminToken").child(district).getValue().toString()
                    val binDetails = BinDetails(area_village,locality,district,loadType,collectionPeriod,lantitude,longitude, id, "Pending")
                    val userName = snapshot.child("User Details").child(idAuth).child("Profile").child("username").getValue()
                    val emailId = snapshot.child("User Details").child(idAuth).child("Profile").child("emailId").getValue()
                    val mobile = snapshot.child("User Details").child(idAuth).child("Profile").child("mobile").getValue()
                    val binExtra = BinExtra(userName as String?, emailId as String?, mobile as String?,idAuth)

                    databaseReference.child("User Details").child(idAuth).child("BINS").child(id.toString()).setValue(binDetails).addOnCompleteListener {
                        databaseReference.child("User Details").child("BinCount").setValue(id).addOnCompleteListener {
                            databaseReference.child("BINS").child(district).child(id.toString()).setValue(binDetails).addOnCompleteListener {
                                databaseReference.child("BINS").child(district).child(id.toString()).child("UserDetails").setValue(binExtra).addOnCompleteListener {
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
                                                        Toast.makeText(this@CreateBin, "Failed ", Toast.LENGTH_LONG).show()
                                                        finish()
                                                    }else
                                                        finish()
                                                }
                                            }

                                            override fun onFailure(call: Call<FCMResponse?>, t: Throwable?) {
                                                finish()
                                            }
                                        })
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


        spinner1 = findViewById<Spinner>(R.id.create_bin_load_type)
        val adapter1 = ArrayAdapter.createFromResource(this,R.array.load_type,android.R.layout.simple_spinner_item)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.setAdapter(adapter1)
        spinner1.setOnItemSelectedListener(this)

        spinner2 = findViewById<Spinner>(R.id.create_bin_collection_periods)
        val adapter2 = ArrayAdapter.createFromResource(this,R.array.collection_period,android.R.layout.simple_spinner_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.setAdapter(adapter2)
        spinner2.setOnItemSelectedListener(this)

        create_bin_back_botton.setOnClickListener {
            finish()
        }

        create_bin_map.setOnClickListener{
            startActivityForResult(Intent(this,MapsActivity::class.java),2858)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==2858){
            create_bin_lantitude.text = data!!.getStringExtra("Lantitude")
            create_bin_longitude.text = data!!.getStringExtra("Longitude")
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val text = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}