package com.cibinenterprizes.cibinenterprises

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditions : AppCompatActivity() {

    var fileName: String = "termsandconditions.html"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        terms_and_conditions_back_botton.setOnClickListener {
            finish()
        }

        val webView = findViewById<WebView>(R.id.terms_and_conditions)
        webView.settings.setJavaScriptEnabled(true)
        webView.loadUrl("file:///android_asset/" + fileName)

    }
}