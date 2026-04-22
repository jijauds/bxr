package com.bxr.trainingapp.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.HowToAdapter
import com.bxr.trainingapp.data.HowToData

class ManualActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home_howtouse)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerHowTo)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = HowToAdapter(HowToData.list)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}