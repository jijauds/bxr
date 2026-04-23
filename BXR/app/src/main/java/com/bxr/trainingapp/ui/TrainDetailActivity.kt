package com.bxr.trainingapp.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.MoveAdapter
import com.bxr.trainingapp.data.MoveRepository
import com.bxr.trainingapp.model.Move
import com.bxr.trainingapp.model.moveScore

class TrainDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_practice_move)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val moveName = intent.getStringExtra("MOVE_NAME")

        val move = MoveRepository.moves.find { it.name == moveName }

        if (move == null) {
            finish()
            return
        }

        val tvName = findViewById<TextView>(R.id.moveDetailTitle)
        val recycler = findViewById<RecyclerView>(R.id.recyclerMoves)

        tvName.text = move.name

        moveScore.refresh(this)
        val bestScore = moveScore.bestScores[move.name] ?: 0

        val actions = listOf(
            Move(
                "Train",
                R.drawable.move,
                "Get up and prepare to record a training session.",
                emptyList(),
                move.videoRes
            ),
            Move(
                "Learn",
                R.drawable.move,
                "Pointers on how to perform this move.",
                emptyList(),
                move.videoRes
            ),
            Move(
                "Logs",
                R.drawable.move,
                "See how well you have been doing this move. Best: $bestScore%",
                emptyList(),
                move.videoRes
            )
        )

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.adapter = MoveAdapter(actions) { action ->

            when (action.name) {

                "Train" -> {
                    startActivity(
                        Intent(this, CameraActivity::class.java).apply {
                            putExtra("MOVE_NAME", move.name)
                        }
                    )
                }

                "Learn" -> {
                    val dialog = Dialog(this)
                    dialog.setContentView(R.layout.dialog_move_learn)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.window?.attributes?.windowAnimations =
                        android.R.style.Animation_Dialog
                    dialog.show()

                    val tvName = dialog.findViewById<TextView>(R.id.tvMoveName)
                    val tvDesc = dialog.findViewById<TextView>(R.id.tvDescription)
                    val tvCritical = dialog.findViewById<TextView>(R.id.tvCritical)
                    val btnVideo = dialog.findViewById<Button>(R.id.btnVideo)
                    val btnTrain = dialog.findViewById<Button>(R.id.btnTrain)
                    val btnClose = dialog.findViewById<Button>(R.id.btnClose)

                    tvName.text = move.name
                    tvDesc.text = move.description
                    tvCritical.text = move.criticalPoints.joinToString("\n• ", prefix = "• ")

                    btnClose.setOnClickListener {
                        dialog.dismiss()
                    }

                    btnVideo.setOnClickListener {
                        startActivity(
                            Intent(this, VideoPlayerActivity::class.java).apply {
                                putExtra("VIDEO_RES_ID", move.videoRes)
                            }
                        )
                    }

                    btnTrain.setOnClickListener {
                        startActivity(
                            Intent(this, CameraActivity::class.java).apply {
                                putExtra("MOVE_NAME", move.name)
                            }
                        )
                    }
                }

                "Logs" -> {
                    startActivity(
                        Intent(this, LogListActivity::class.java).apply {
                            putExtra("PUNCH_TYPE", move.name)
                        }
                    )
                }
            }
        }
    }
}