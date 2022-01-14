package com.cibinenterprizes.cibinenterprises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.cibinenterprizes.cibinenterprises.Model.BinDetails
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
import kotlinx.android.synthetic.main.activity_update_bin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateBin : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binIdForUpdate: String
    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var database = FirebaseDatabase.getInstance().reference
    lateinit var ifcmService: IFCMService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_bin)

        ifcmService = RetrofitFCMClient.getInstance("https://fcm.googleapis.com/")
            .create(IFCMService::class.java)

        val spinner1 = findViewById<Spinner>(R.id.update_bin_load_type)
        val adapter1 = ArrayAdapter.createFromResource(
            this,
            R.array.load_type,
            android.R.layout.simple_spinner_item
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.setAdapter(adapter1)
        spinner1.setOnItemSelectedListener(this)

        val spinner2 = findViewById<Spinner>(R.id.update_bin_collection_periods)
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.collection_period,
            android.R.layout.simple_spinner_item
        )
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.setAdapter(adapter2)
        spinner2.setOnItemSelectedListener(this)

        binIdForUpdate = getIntent().extras?.get("Bin ID").toString()
        Toast.makeText(this, binIdForUpdate, Toast.LENGTH_SHORT).show()

        RetriveBinData()
        update_bin_back_botton.setOnClickListener {
            finish()
        }
        update_bin_botton.setOnClickListener {
            updateBin(spinner1, spinner2)
        }
        update_bin_map.setOnClickListener {
            startActivityForResult(Intent(this, MapsActivity::class.java), 2858)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2858) {
            update_bin_lantitude.text = data!!.getStringExtra("Lantitude")
            update_bin_longitude.text = data!!.getStringExtra("Longitude")
        }
    }

    private fun updateBin(spinner1: Spinner, spinner2: Spinner) {
        val area = update_bin_area.text.toString()
        val locality = update_bin_locality.text.toString()
        val city = update_bin_city.text.toString()
        val loadType = spinner1.getSelectedItem().toString()
        val collectionPeriod = spinner2.getSelectedItem().toString()
        val lantitude = update_bin_lantitude.text.toString()
        val longitude = update_bin_longitude.text.toString()
        val binDetails = BinDetails(
            area,
            locality,
            city,
            loadType,
            collectionPeriod,
            lantitude,
            longitude,
            binIdForUpdate.toInt(),
            "Pending"
        )
        database.child("User Details").child(user?.uid.toString()).child("BINS")
            .child(binIdForUpdate).setValue(binDetails).addOnCompleteListener {
            database.child("BINS").child(binIdForUpdate).setValue(binDetails)
                .addOnCompleteListener {
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val district = snapshot.child("User Details").child(user?.uid.toString()).child("Profile").child("district").getValue().toString()
                            val adminToken = snapshot.child("AdminToken").child(district).getValue().toString()
                            val dataSend = HashMap<String, String>()
                            dataSend.put("title", "Bin Status")
                            dataSend.put(
                                "content",
                                "Bin ID $binIdForUpdate is changed verify it."
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
                                                Toast.makeText(this@UpdateBin, "Failed ", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }

    private fun RetriveBinData() {
        var getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var area = snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                    .child(binIdForUpdate).child("area_Village").getValue().toString()
                var locality =
                    snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                        .child(binIdForUpdate).child("locality").getValue().toString()
                var city = snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                    .child(binIdForUpdate).child("district").getValue().toString()
                var loadType =
                    snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                        .child(binIdForUpdate).child("loadType").getValue().toString()
                var collectionPeriod =
                    snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                        .child(binIdForUpdate).child("collectionPeriod").getValue().toString()
                var lantitude =
                    snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                        .child(binIdForUpdate).child("lantitude").getValue().toString()
                var longitude =
                    snapshot.child("User Details").child(user?.uid.toString()).child("BINS")
                        .child(binIdForUpdate).child("longitude").getValue().toString()

                if (loadType == "High") {
                    update_bin_load_type.setSelection(1)
                } else {
                    update_bin_load_type.setSelection(2)
                }
                if (collectionPeriod == "Daily") {
                    update_bin_collection_periods.setSelection(1)
                } else {
                    update_bin_collection_periods.setSelection(2)
                }

                update_bin_area.setText(area)
                update_bin_locality.setText(locality)
                update_bin_city.setText(city)
                update_bin_lantitude.setText(lantitude)
                update_bin_longitude.setText(longitude)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}