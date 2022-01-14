package com.cibinenterprizes.cibinenterprises


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cibinenterprizes.cibinenterprises.Common.Common
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.network_alart_dialog.*
import org.jsoup.Jsoup
import java.io.IOException

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener  {

    private var backPressedTime = 0L
    private lateinit var auth: FirebaseAuth
    lateinit var spinner: Spinner
    var user = FirebaseAuth.getInstance().currentUser
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        FirebaseInstanceId.getInstance().instanceId.addOnFailureListener { e ->
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                Common.updateToken(this, it.result?.token.toString())
                //Toast.makeText(this, it.result?.token.toString(),Toast.LENGTH_SHORT).show()
            }
        }

        checkConnection()
        auth = FirebaseAuth.getInstance()

        //App Update
        val appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                        && appUpdateInfo.updatePriority() >= 5){
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,this,2858)
            }
        }
        //App Update

        val manager = ReviewManagerFactory.create(applicationContext)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener {requests ->
            if (requests.isSuccessful){
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener {
                    Log.i("HomeActivity", "Status provided")
                }
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }


        var getData = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var userName =
                    snapshot.child("User Details").child(user?.uid.toString()).child("Profile")
                        .child("username").getValue().toString()
                var emailId =
                    snapshot.child("User Details").child(user?.uid.toString()).child("Profile")
                        .child("emailId").getValue().toString()
                var profilePhoto =
                    snapshot.child("User Details").child(user?.uid.toString()).child("Profile")
                        .child("profilePhoto").getValue().toString()
                menu_user_name.setText(userName)
                menu_email_id.setText(emailId)
                Picasso.get().load(profilePhoto).into(menu_profile_image)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)

        val frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom)

        home_background.animate().translationY(-700f).setDuration(800).setStartDelay(2000)
        home_welcome.animate().translationY(150f).alpha(0f).setDuration(800).setStartDelay(2000)
        home_cibin.startAnimation(frombottom)
        home_screen_list.startAnimation(frombottom)
        val drawerLayout = findViewById<DrawerLayout>(R.id.home_drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.home_nav_view)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.bringToFront()

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        home_create_bin.setOnClickListener {
            startActivity(Intent(this, CreateBin::class.java))
        }
        home_update_bin.setOnClickListener {
            startActivity(Intent(this, BinListUpdate::class.java))
        }
        home_register_complaints.setOnClickListener {
            startActivity(Intent(this, RegisterComplaints::class.java))
        }
        home_my_complaints.setOnClickListener {
            startActivity(Intent(this, MyComplaints::class.java))
        }
        home_my_profile.setOnClickListener {
            startActivity(Intent(this, MyProfile::class.java))
        }

    }
    //App Update
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2858){
            Toast.makeText(this, "Start Download", Toast.LENGTH_SHORT).show()
            if (resultCode != RESULT_OK){
                Log.i("HomeActivity", "Update flow failed! Result code: $resultCode")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //App Update

    private fun checkConnection() {

        val manager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (null == networkInfo){
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.network_alart_dialog)

            dialog.setCanceledOnTouchOutside(false)

            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.btn_try_again.setOnClickListener {
                recreate()
            }
            dialog.show()
        }
    }

    override fun onBackPressed() {
        if (home_drawer_layout.isDrawerOpen(GravityCompat.START)){
            home_drawer_layout.closeDrawer(GravityCompat.START)
        }else if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishAffinity()
        }else{
            Toast.makeText(this,"Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu_home -> home_drawer_layout.closeDrawer(GravityCompat.START)
            R.id.menu_bin_list -> binList()
            R.id.menu_about_us -> startActivity(Intent(this, AboutUs::class.java))
            R.id.menu_contact_support -> startActivity(Intent(this, ContactSupport::class.java))
            R.id.menu_terms_and_conditions -> startActivity(Intent(this, TermsAndConditions::class.java))
            R.id.menu_version -> startActivity(Intent(this, AppVersion::class.java))
            R.id.menu_logout -> signoutOperation()
        }

        return true
    }
    fun binList(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var districtName = snapshot.child("User Details").child(user?.uid.toString()).child("Profile").child("district").getValue().toString()
                var intent = Intent(this@HomeActivity, ListOfBin::class.java)
                intent.putExtra("District", districtName)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun signoutOperation(){
        auth.signOut()
        startActivity(Intent(this,Login::class.java))
        finish()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}