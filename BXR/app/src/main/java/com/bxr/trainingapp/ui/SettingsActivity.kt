package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.ui.HomeActivity
import com.bxr.trainingapp.R
import android.widget.RadioButton
import com.bxr.trainingapp.data.JsonWriter
import android.widget.Button

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val btnOrthodox = findViewById<RadioButton>(R.id.right_handed)
        val btnSouthpaw = findViewById<RadioButton>(R.id.left_handed)
        val btnFront = findViewById<RadioButton>(R.id.front_camera)
        val btnRear = findViewById<RadioButton>(R.id.rear_camera)
        val spaceName = findViewById<EditText>(R.id.editTextText)
        val btnSave = findViewById<Button>(R.id.btnSave)

        if(getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("HANDEDNESS", "ORTHODOX") == "ORTHODOX"){
            btnOrthodox.isChecked = true
        } else {
            btnSouthpaw.isChecked = true
        }

        if(getSharedPreferences("USER_PREFS", MODE_PRIVATE).getInt("CAMERA_FACING", 0) == 0){
            btnFront.isChecked = true
        } else {
            btnRear.isChecked = true
        }

        spaceName.setText(getSharedPreferences("USER_PREFS", MODE_PRIVATE).getString("NAME", "Name"))

        btnFront.setOnClickListener {
            saveCamera(0)
        }

        btnRear.setOnClickListener {
            saveCamera(1)
        }

        btnOrthodox.setOnClickListener {
            saveHandedness("ORTHODOX")
        }

        btnSouthpaw.setOnClickListener {
            saveHandedness("SOUTHPAW")
        }

        findViewById<Button>(R.id.btnClearData).setOnClickListener {
            showDeleteConfirmation()
        }

        btnSave.setOnClickListener {
            goToHome(spaceName.text.toString())
        }
    }

    private fun saveCamera(type: Int) {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE) ?: return
        with (prefs.edit()) {
            putInt("CAMERA_FACING", type)
            commit()
        }
    }

    private fun saveHandedness(type: String) {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE) ?: return
        with (prefs.edit()) {
            putString("HANDEDNESS", type)
            commit()
        }
    }

    private fun goToHome(type: String) {
        val prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE) ?: return
        with (prefs.edit()) {
            putString("NAME", type)
            commit()
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showDeleteConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Clear All Data")
            .setMessage("Are you sure you want to delete all training logs? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                JsonWriter(this.applicationContext).clearData()
                android.widget.Toast.makeText(this, "All logs cleared", android.widget.Toast.LENGTH_SHORT).show()
                // finish()
                // startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
