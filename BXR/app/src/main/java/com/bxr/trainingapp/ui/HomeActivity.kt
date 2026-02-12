package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        val tvUsername = findViewById<TextView>(R.id.tv_username)
        val btnTrain = findViewById<Button>(R.id.train_button)
        val btnSettings = findViewById<Button>(R.id.settings_button)


        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        val handedness = prefs.getString("HANDEDNESS", "ORTHODOX")
        val name = prefs.getString("NAME", "User")

        tvUsername.text = "Welcome, $name!"

        btnTrain.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, HandednessActivity::class.java)
            startActivity(intent)
        }
    }
}
