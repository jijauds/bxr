package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.LogRepository
import com.bxr.trainingapp.model.moveScore

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home)

        // val tvUsername = findViewById<TextView>(R.id.tv_username)
        val btnTrain = findViewById<ImageButton>(R.id.train_button)
        // val btnLogs = findViewById<Button>(R.id.logs_button)
        val btnSettings = findViewById<Button>(R.id.settings_button)
        val btnAbout = findViewById<Button>(R.id.about_button)
        val btnManual = findViewById<Button>(R.id.manual_button)

        // val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        // val name = prefs.getString("NAME", "User")

//        tvUsername.text = buildString {
//            append("Welcome, ")
//            append(name)
//            append("!")
//        }

        moveScore.refresh(this)

        btnTrain.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

//        btnLogs.setOnClickListener {
//            val intent = Intent(this, LogsActivity::class.java)
//            startActivity(intent)
//        }

        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        btnManual.setOnClickListener {
            val intent = Intent(this, ManualActivity::class.java)
            startActivity(intent)
        }
    }
}
