package com.cibinenterprizes.cibinenterprises

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_app_version.*

class AppVersion : AppCompatActivity() {

    lateinit var currentVersion: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_version)

        currentVersion = findViewById(R.id.app_version_code)

        var versionCodes: String = BuildConfig.VERSION_NAME
        currentVersion.setText(versionCodes)

        app_version_back_botton.setOnClickListener {
            finish()
        }
    }
}