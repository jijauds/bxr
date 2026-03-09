package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R

class PostCameraActivity : AppCompatActivity() {

    private var moveName: String? = null
    private var score: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.session_done)

        moveName = intent.getStringExtra("MOVE_NAME")
        score = intent.getIntExtra("SCORE", 0)

        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvLogDetails = findViewById<TextView>(R.id.tvLogDetails)
        val tvMovesList = findViewById<TextView>(R.id.tvMovesList)
        val tvBackHome = findViewById<TextView>(R.id.tvBackHome)

        tvScore.text = buildString {
            append(score)
            append("%")
        }

        tvLogDetails.setOnClickListener {
            val intent = Intent(this, LogListActivity::class.java)
            intent.putExtra("PUNCH_TYPE", moveName)
            startActivity(intent)
        }

        tvMovesList.setOnClickListener {
            val intent = Intent(this, TrainActivity::class.java)
            startActivity(intent)
        }

        tvBackHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("MOVE_NAME", moveName)
            startActivity(intent)
        }
    }
}