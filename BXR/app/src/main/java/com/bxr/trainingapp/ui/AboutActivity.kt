package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R
import androidx.core.net.toUri

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home_about)

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val btnManuscript = findViewById<Button>(R.id.manuscript_button)

        btnBack.setOnClickListener {
            finish()
        }

        btnManuscript.setOnClickListener {

            val manuscriptUrl =
                "https://docs.google.com/document/d/1Eln1VXThQTwicdTde8nDsMO9PFlowIJTotg2gu3cf9Q/edit?usp=sharing"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = manuscriptUrl.toUri()
            }

        }
    }
}