package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.MoveAdapter
import com.bxr.trainingapp.model.Move

class TrainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moves_list)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val recycler = findViewById<RecyclerView>(R.id.recyclerMoves)

        val moves = listOf(
            Move("Jab", R.drawable.move,
                "Straight punch to the front with your non-dominant hand.",
                R.raw.jab_preview),

            Move("Straight", R.drawable.move,
                "Straight punch with your dominant hand.",
                R.raw.straight_preview),

            Move("Lead Hook", R.drawable.move,
                "Curled punch with your non-dominant hand.",
                R.raw.jab_preview),

            Move("Rear Hook", R.drawable.move,
                "Curled punch with your dominant hand.",
                R.raw.jab_preview)
        )

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