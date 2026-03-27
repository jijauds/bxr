package com.bxr.trainingapp.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.LogRepository

class LogListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        val recyclerLogs = findViewById<RecyclerView>(R.id.recyclerMoves)
        val tvEmptyMessage = findViewById<TextView>(R.id.tvEmptyMessage)

        recyclerLogs.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        recyclerLogs.setPadding(0, 0, 0, 0)

        findViewById<View>(R.id.layoutDots).visibility = View.GONE
        findViewById<View>(R.id.btnView).visibility = View.GONE
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val punchType = intent.getStringExtra("PUNCH_TYPE") ?: "Jab"
        findViewById<TextView>(R.id.tvMoveTitle).text = punchType

        val allLogs = try {
            LogRepository.loadLogs(this)
        } catch (e: Exception) {
            emptyList()
        }

        val logs = allLogs.filter { it.punchType == punchType }

        if (logs.isEmpty()) {
            recyclerLogs.visibility = View.GONE
            tvEmptyMessage.visibility = View.VISIBLE
        } else {
            tvEmptyMessage.visibility = View.GONE
            recyclerLogs.visibility = View.VISIBLE

            recyclerLogs.layoutManager = LinearLayoutManager(this)
            recyclerLogs.adapter = LogAdapter(logs)

            val params = recyclerLogs.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            recyclerLogs.layoutParams = params
            recyclerLogs.setPadding(0, 0, 0, 0)
        }
    }
}