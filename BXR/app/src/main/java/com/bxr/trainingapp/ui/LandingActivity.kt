package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing)

        val btnStart = findViewById<Button>(R.id.start_button)

        btnStart.setOnClickListener {
            navigateNext()
        }
    }

    private fun navigateNext() {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val handedness = prefs.getString("HANDEDNESS", null)

        val intent = if (handedness == null) {
            Intent(this, HandednessActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}
