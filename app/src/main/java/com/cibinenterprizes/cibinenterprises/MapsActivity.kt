package com.cibinenterprizes.cibinenterprises

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        if(requestCode == LOCATION_PERMISSION_REQUEST)
        {
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED))
            {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED)
                {
                    mMap.isMyLocationEnabled = true
                    return
                }

            }else{
                Toast.makeText(this,"User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
        getLocationAccess()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(11.057492467264849,77.41389948874712)))
        mMap.setOnMapClickListener {latLng ->

            val marker = mMap.addMarker(MarkerOptions()
                .position(latLng)
                .title("ORDER REQUEST")
                .snippet("location"))
            val dialog = AlertDialog.Builder(this)
                .setTitle("CONFORM LOCATION").setMessage("click 'OK' to 'comform' or 'CANCEL'")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", null)
                .show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
                var intent = Intent()
                intent.putExtra("Lantitude", marker.position.latitude.toString())
                intent.putExtra("Longitude", marker.position.longitude.toString())
                setResult(2858, intent)
                finish()
            }
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                var intent = Intent()
                intent.putExtra("Lantitude", "")
                intent.putExtra("Longitude", "")
                setResult(2858, intent)
                finish()
            }
        }
    }
    private fun getLocationAccess()
    {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
        }
    }
}