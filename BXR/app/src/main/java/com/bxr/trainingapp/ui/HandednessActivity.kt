package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.ui.HomeActivity
import com.bxr.trainingapp.R
import android.widget.RadioButton

class HandednessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.handedness)

        val btnOrthodox = findViewById<RadioButton>(R.id.left_handed)
        val btnSouthpaw = findViewById<RadioButton>(R.id.right_handed)
        val etName = findViewById<EditText>(R.id.editTextText)

        btnOrthodox.setOnClickListener {
            saveHandedness("ORTHODOX", etName.text.toString())
            goToHome()
        }

        btnSouthpaw.setOnClickListener {
            saveHandedness("SOUTHPAW", etName.text.toString())
            goToHome()
        }
    }

    private fun saveHandedness(type: String, name: String) {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        prefs.edit().putString("HANDEDNESS", type).apply()
        prefs.edit().putString("NAME", name).apply()
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // prevents going back to this screen
    }
}
