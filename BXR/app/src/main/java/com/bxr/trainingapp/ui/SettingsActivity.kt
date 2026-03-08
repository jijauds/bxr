package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
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

        val btnOrthodox = findViewById<RadioButton>(R.id.left_handed)
        val btnSouthpaw = findViewById<RadioButton>(R.id.right_handed)
        val setName = findViewById<EditText>(R.id.editTextText)
        val btnSave = findViewById<Button>(R.id.btnSave)


        btnOrthodox.setOnClickListener {
            saveHandedness("ORTHODOX", setName.text.toString())
        }

        btnSouthpaw.setOnClickListener {
            saveHandedness("SOUTHPAW", setName.text.toString())
        }

        findViewById<Button>(R.id.btnClearData).setOnClickListener {
            showDeleteConfirmation()
        }

        btnSave.setOnClickListener {
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
