package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.MoveAdapter
import com.bxr.trainingapp.data.MoveRepository
import com.bxr.trainingapp.model.moveScore

class TrainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home_practice)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val recycler = findViewById<RecyclerView>(R.id.recyclerMoves)

        val moves = MoveRepository.moves

        moveScore.refresh(this)

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.adapter = MoveAdapter(moves) { move ->
            val intent = Intent(this, TrainDetailActivity::class.java)
            intent.putExtra("MOVE_NAME", move.name)
            intent.putExtra("MOVE_DESC", move.description)
            intent.putExtra("MOVE_VIDEO", move.videoRes)
            startActivity(intent)
        }
    }
}