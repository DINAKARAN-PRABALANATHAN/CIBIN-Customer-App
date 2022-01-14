package com.cibinenterprizes.cibinenterprises

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cibinenterprizes.cibinenterprises.Adapter.CustomInfoWindowForGoogleMap
import com.cibinenterprizes.cibinenterprises.Model.BinDetails
import com.cibinenterprizes.cibinenterprises.Model.BinMap
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_list_of_bin.*

class ListOfBin : AppCompatActivity(), OnMapReadyCallback, LocationListener {


    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1
    var marker: Marker? = null
    var districtName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_bin)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        districtName = getIntent().extras?.get("District").toString()
        var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("BINS").child(districtName!!)
        databaseReference.push().setValue(marker)
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
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)
        getLocationAccess()
        districtName = getIntent().extras?.get("District").toString()
        var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("BINS").child(districtName!!)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    var user: BinMap? = i.getValue(BinMap::class.java)
                    var location: LatLng = LatLng(user!!.Lantitude!!.toDouble(),user!!.Longitude!!.toDouble())
                    var snippet = user.Verification+"\nDriver : "+user.DriverName
                    mMap.addMarker(MarkerOptions().position(location).title(user.BinId.toString())).setSnippet(snippet)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                    mMap.setInfoWindowAdapter(CustomInfoWindowForGoogleMap(this@ListOfBin))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun getLocationAccess()
    {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.isMyLocationEnabled = true
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)
        }
    }

    override fun onLocationChanged(p0: Location) {

    }
}