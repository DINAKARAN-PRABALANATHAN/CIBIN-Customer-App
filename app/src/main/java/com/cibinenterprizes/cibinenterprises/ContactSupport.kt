package com.cibinenterprizes.cibinenterprises

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_contact_support.*

class ContactSupport : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var auth: FirebaseAuth
    private var REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_support)
        auth = FirebaseAuth.getInstance()

        val instagram = findViewById<ImageView>(R.id.instagram)
        val youtube = findViewById<ImageView>(R.id.youtube)
        val twitter = findViewById<ImageView>(R.id.twitter)
        val linkedin = findViewById<ImageView>(R.id.linkedin)
        val facebook = findViewById<ImageView>(R.id.facebook)

        instagram.setOnClickListener {
            gotoUrl("https://instagram.com/cibin_cigarette_recycling?igshid=gx4zoolh3h9c")
        }
        youtube.setOnClickListener {
            gotoUrl("https://www.youtube.com/channel/UCYzbghOUjyrYtsHrugdmTYw?view_as=subscriber")
        }
        twitter.setOnClickListener {
            gotoUrl("https://twitter.com/CEnterprizes")
        }
        linkedin.setOnClickListener {
            gotoUrl("https://www.linkedin.com/in/cibin-enterprizes-09831a1b7/")
        }
        facebook.setOnClickListener {
            gotoUrl("https://www.facebook.com/cibinenterprizes")
        }
        contact_support_botton.setOnClickListener {
            finish()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_location) as SupportMapFragment
        mapFragment.getMapAsync(this)
        imageButton.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                makePhoneCall()
            }else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(this as ContactSupport,Manifest.permission.CALL_PHONE)){
                    Toast.makeText(this,"Phone Call permission is need to call by click call botton", Toast.LENGTH_SHORT).show()
                }
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),REQUEST_CODE)

            }


        }
        imageButton2.setOnClickListener {
            val recipient = "cibinenterprize@gmail.com"
            sendEmail(recipient)
        }
    }

    private fun makePhoneCall() {
        val number: String = "9597417946"
        val intent = Intent(Intent.ACTION_CALL)
        intent.data= Uri.parse("tel: $number")
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE){
            if(grantResults.size>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                makePhoneCall()
            }else{
                Toast.makeText(this,"Permission was not granted", Toast.LENGTH_SHORT).show()
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    private fun gotoUrl(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
    private fun sendEmail(recipient: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))

        try {
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            mMap = p0
        }
        val sydney = LatLng(11.057492467264849,77.41389948874712)
        mMap.addMarker(MarkerOptions().position(sydney).title("CIBIN Enterprizes"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}