package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.JsonWriter

class SettingsActivity : AppCompatActivity() {

    private lateinit var prefs: android.content.SharedPreferences

    private lateinit var btnHandedness: TextView
    private lateinit var btnCamera: TextView
    private lateinit var btnName: TextView
    private lateinit var checkOverlay: CheckBox

    private var userName = "Name"
    private var handedness = "ORTHODOX"
    private var cameraFacing = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home_settings)

        prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE)

        btnHandedness = findViewById(R.id.btnHandedness)
        btnCamera = findViewById(R.id.btnCamera)
        btnName = findViewById(R.id.setTextText)
        checkOverlay = findViewById(R.id.checkOverlay)

        val btnSave = findViewById<Button>(R.id.btnSave)

        loadPrefs()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        btnHandedness.setOnClickListener {
            showHandednessDialog()
        }

        btnCamera.setOnClickListener {
            showCameraDialog()
        }

        btnName.setOnClickListener {
            showNameDialog()
        }

        btnSave.setOnClickListener {
            saveAll()
        }

        findViewById<Button>(R.id.btnClearData).setOnClickListener {
            showDeleteConfirmation()
        }

        checkOverlay.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit()
                .putBoolean("SHOW_OVERLAY", isChecked)
                .apply()
        }
    }

    private fun loadPrefs() {
        handedness = prefs.getString("HANDEDNESS", "ORTHODOX")!!
        cameraFacing = prefs.getInt("CAMERA_FACING", 0)
        userName = prefs.getString("NAME", "Name")!!

        val showOverlay = prefs.getBoolean("SHOW_OVERLAY", true) // default ON

        btnHandedness.text =
            if (handedness == "ORTHODOX") "Right-handed" else "Left-handed"

        btnCamera.text =
            if (cameraFacing == 0) "Front" else "Rear"

        btnName.text = userName

        checkOverlay.isChecked = showOverlay
    }

    private fun saveAll() {
        with(prefs.edit()) {
            putString("HANDEDNESS", handedness)
            putInt("CAMERA_FACING", cameraFacing)
            putString("NAME", userName)
            putBoolean("SHOW_OVERLAY", checkOverlay.isChecked) // important
            apply()
        }

        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun showHandednessDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_settings_handedness, null)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        view.findViewById<Button>(R.id.btnLeft).setOnClickListener {
            handedness = "SOUTHPAW"
            btnHandedness.text = "Left-handed"
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btnRight).setOnClickListener {
            handedness = "ORTHODOX"
            btnHandedness.text = "Right-handed"
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCameraDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_settings_camera, null)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        view.findViewById<Button>(R.id.btnFront).setOnClickListener {
            cameraFacing = 0
            btnCamera.text = "Front"
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.btnRear).setOnClickListener {
            cameraFacing = 1
            btnCamera.text = "Rear"
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNameDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_settings_name, null)
        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        val editName = view.findViewById<EditText>(R.id.editName)
        val btnSave = view.findViewById<Button>(R.id.btnConfirmName)

        editName.setText(userName)

        btnSave.setOnClickListener {
            userName = editName.text.toString()
            btnName.text = userName
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Clear All Data")
            .setMessage("Are you sure you want to delete all training logs? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                JsonWriter(this.applicationContext).clearData()
                android.widget.Toast.makeText(this, "All logs cleared", android.widget.Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}