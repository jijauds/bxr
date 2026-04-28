package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.LogRepository
import com.bxr.trainingapp.sessions.RepResult

class PostCameraActivity : AppCompatActivity() {

    private var moveName: String? = null
    private var score: Int? = null
    private var reps: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_move_train_done)

        moveName = intent.getStringExtra("MOVE_NAME")
        score = intent.getIntExtra("SCORE", 0)
        reps = intent.getIntExtra("REPS", 0)
        val logId = intent.getIntExtra("LOG_ID", -1)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val tvReps = findViewById<TextView>(R.id.tvReps)
        val tvLogDetails = findViewById<TextView>(R.id.tvLogDetails)
        val tvMovesList = findViewById<TextView>(R.id.tvMovesList)
        val tvBackHome = findViewById<TextView>(R.id.tvBackHome)
        val tvErrors = findViewById<TextView>(R.id.tvErrors)


        tvScore.text = buildString {
            append(score)
            append("%")
        }

        tvReps.text = buildString {
            append(reps)
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

        val logs = LogRepository.loadLogs(this)
        val log = logs.find { it.id == logId.toInt() }

        if (log == null) {
            tvLogDetails.text = "Log not found"
            return
        }

        val repResults = log.repResults

        val allErrors = repResults
            .flatMap { it.errors }
            .groupBy { it.message }

        Log.d("FLATTENED_ERRORS",
            repResults.flatMap { it.errors }
                .joinToString { it.message }
        )

        tvErrors.text = when {
            repResults.isEmpty() -> "No data."
            allErrors.isEmpty() -> "Perfect form."
            else -> "Errors:\n" + allErrors.entries.joinToString("\n")
//            else -> allErrors.entries.joinToString("\n") { (message, count) ->
//                "$message (${count}x)"
//            }
        }

//        findViewById<Button>(R.id.btnStart).setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra("MOVE_NAME", moveName)
//            startActivity(intent)
//        }
    }
}