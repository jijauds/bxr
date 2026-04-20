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
import com.bxr.trainingapp.adapter.MoveAdapter
import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.Move

class TrainDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_detail)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val moveName = intent.getStringExtra("MOVE_NAME")
        val moveDesc = intent.getStringExtra("MOVE_DESC")
        val moveVideo = intent.getIntExtra("MOVE_VIDEO", -1)

        val tvName = findViewById<TextView>(R.id.moveDetailTitle)
        val recycler = findViewById<RecyclerView>(R.id.recyclerMoves)

        tvName.text = moveName

        val actions = listOf(
            Move("Train", R.drawable.move, "Get up and prepare to record a training session.", R.raw.jab_preview),
            Move("Learn", R.drawable.move, "Pointers on how to perform a jab.", R.raw.jab_preview),
            Move("Logs", R.drawable.move, "See hoe well you have been doing this move.", R.raw.jab_preview)
        )

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.adapter = MoveAdapter(actions) { action ->
            when (action.name) {

                "Train" -> {
                    val intent = Intent(this, CameraActivity::class.java)
                    intent.putExtra("MOVE_NAME", moveName)
                    startActivity(intent)
                }

                "Learn" -> {
                    val dialog = Dialog(this)
                    dialog.setContentView(R.layout.dialog_learn)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
                    dialog.show()

                    val tvName = dialog.findViewById<TextView>(R.id.tvMoveName)
                    val tvDesc = dialog.findViewById<TextView>(R.id.tvDescription)
                    val tvCritical = dialog.findViewById<TextView>(R.id.tvCritical)
                    val btnVideo = dialog.findViewById<Button>(R.id.btnVideo)
                    val btnTrain = dialog.findViewById<Button>(R.id.btnTrain)

                    tvName.text = moveName
                    tvDesc.text = moveDesc
                    tvCritical.text = moveDesc

                    btnVideo.setOnClickListener {
                        val intent = Intent(this, VideoPlayerActivity::class.java)
                        intent.putExtra("VIDEO_RES_ID", moveVideo)
                        startActivity(intent)
                    }

                    btnTrain.setOnClickListener {
                        val intent = Intent(this, CameraActivity::class.java)
                        intent.putExtra("MOVE_NAME", moveName)
                        startActivity(intent)
                    }
                }

                "Logs" -> {
                    val intent = Intent(this, LogListActivity::class.java)
                    intent.putExtra("PUNCH_TYPE", moveName)
                    startActivity(intent)
                }
            }
        }
    }
}