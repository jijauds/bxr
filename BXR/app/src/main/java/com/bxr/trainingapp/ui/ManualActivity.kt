package com.bxr.trainingapp.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R

class ManualActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        val btnGeneral = findViewById<Button>(R.id.btn_general)
        val btnCameraManual = findViewById<Button>(R.id.btn_camera_manual)
        val btnTraining = findViewById<Button>(R.id.btn_training)
        val btnErrors = findViewById<Button>(R.id.btn_errors)

        fun showPopup(layout: Int) {
            val dialog = Dialog(this)
            dialog.setContentView(layout)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
            dialog.show()
        }

        btnGeneral.setOnClickListener {
            showPopup(R.layout.dialog_general)
        }

        btnCameraManual.setOnClickListener {
            showPopup(R.layout.dialog_camera_manual)
        }

        btnTraining.setOnClickListener {
            showPopup(R.layout.dialog_training)
        }

        btnErrors.setOnClickListener {
            showPopup(R.layout.dialog_errors)
        }

    }

}