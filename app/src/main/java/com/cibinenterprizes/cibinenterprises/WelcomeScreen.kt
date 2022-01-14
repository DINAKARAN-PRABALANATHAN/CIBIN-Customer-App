package com.cibinenterprizes.cibinenterprises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class WelcomeScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var handler: Handler

    //Animation

    lateinit var topAnim: Animation
    lateinit var bottomAnim:Animation
    lateinit var imageView: ImageView
    lateinit var welcomemgs: TextView
    lateinit var slogan: TextView

    //Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome_screen)
        //Animation

        //topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        //bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation)

        imageView = findViewById(R.id.loco_welcome_screen)
        welcomemgs = findViewById(R.id.welcome_msg)
        slogan = findViewById(R.id.services)

        //imageView.startAnimation(topAnim)
        //welcomemgs.startAnimation(bottomAnim)
        //slogan.startAnimation(bottomAnim)


        //Animation

        handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, IntroSlideShow::class.java))
            finish()
        }, 5000)

        auth = FirebaseAuth.getInstance()

    }
}