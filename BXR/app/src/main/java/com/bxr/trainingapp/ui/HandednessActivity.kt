package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R
import com.google.android.material.card.MaterialCardView

class HandednessActivity : AppCompatActivity() {

    private var selectedHand: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_handedness)

        val btnLeft = findViewById<MaterialCardView>(R.id.btnLeft)
        val btnRight = findViewById<MaterialCardView>(R.id.btnRight)
        val textLeft = findViewById<TextView>(R.id.textLeft)
        val textRight = findViewById<TextView>(R.id.textRight)
        val btnNext = findViewById<ImageButton>(R.id.btnNext)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        // val setName = findViewById<EditText>(R.id.editTextText)

        btnLeft.setOnClickListener {
            selectedHand = "ORTHODOX"

            btnLeft.setCardBackgroundColor(getColor(R.color.white))
            btnRight.setCardBackgroundColor(getColor(R.color.gray))

            btnLeft.strokeColor = getColor(R.color.white)
            btnRight.strokeColor = getColor(R.color.light_gray)

            textLeft.setTextColor(getColor(R.color.black))
            textRight.setTextColor(getColor(R.color.light_gray))
        }

        btnRight.setOnClickListener {
            selectedHand = "SOUTHPAW"

            btnRight.setCardBackgroundColor(getColor(R.color.white))
            btnLeft.setCardBackgroundColor(getColor(R.color.gray))

            btnRight.strokeColor = getColor(R.color.white)
            btnLeft.strokeColor = getColor(R.color.light_gray)

            textRight.setTextColor(getColor(R.color.black))
            textLeft.setTextColor(getColor(R.color.light_gray))
        }

        btnNext.setOnClickListener {
            // val name = setName.text.toString()

            if (selectedHand != null) {
                saveHandedness(selectedHand!!, "")
                goToHome()
            } else {
                Toast.makeText(this, "Select your dominant hand", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            goToLanding()
        }
    }

    private fun saveHandedness(type: String, name: String) {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
        prefs.edit().putString("HANDEDNESS", type).apply()
        prefs.edit().putString("NAME", name).apply()
    }

    private fun goToLanding() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}