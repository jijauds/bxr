package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.PagerSnapHelper
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.CarouselAdapter
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.forms.trackJab
import com.bxr.trainingapp.model.Move
import android.net.Uri
import android.widget.MediaController
import androidx.core.net.toUri
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Handedness
import com.bxr.trainingapp.sessions.SessionTracker
import java.time.Instant

class TrainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvMoveTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvPreview: VideoView
    private lateinit var layoutDots: LinearLayout

    private lateinit var moves: List<Move>
    private var currentPosition = 0
    private var currentMove: Move? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moves_list)

        recyclerView = findViewById(R.id.recyclerMoves)
        tvMoveTitle = findViewById(R.id.tvMoveTitle)
        layoutDots = findViewById(R.id.layoutDots)
        tvDescription = findViewById(R.id.tvMoveDescription)
        tvPreview = findViewById(R.id.tvMovePreview)

        tvDescription.visibility = View.GONE
        tvPreview.visibility = View.GONE

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val guard_desc = "\nGuarding: \n\nChin tucked downwards\nRelaxed and leveled shoulders\nLead arm slightly bent, hand in front of face\nRear arm bent and close to chin\nHips square with shoulders\nLegs bent"

        moves = listOf(
            Move("Jab", R.drawable.move,
                "$guard_desc\n\nAt climax: \n\nLead arm fully extended\nRear arm at guard\nHips and legs same as guard",
                R.raw.jab_preview
            ),
            Move("Straight", R.drawable.move,
                "$guard_desc\n\nAt climax: \n\nLead arm fully extended\nRear arm at guard\nHips and legs same as guard",
                R.raw.straight_preview
            ),
            Move("Front Hook", R.drawable.move,
                "$guard_desc\n\nAt climax: \n\nLead arm fully extended\nRear arm at guard\nHips and legs same as guard",
                R.raw.jab_preview
            ),
            Move("Front Uppercut", R.drawable.move,
                "$guard_desc\n\nAt climax: \n\nLead arm fully extended\nRear arm at guard\nHips and legs same as guard",
                R.raw.front_uppercut_preview
            ),
            Move("Rear Uppercut", R.drawable.move,
                "$guard_desc\n\nAt climax: \n\nLead arm fully extended\nRear arm at guard\nHips and legs same as guard",
                R.raw.rear_uppercut_preview
            )
        )

        setupRecycler()
        setupDots()
        updateUI(0)

        findViewById<Button>(R.id.btnTrain).setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("MOVE_NAME", moves[currentPosition].name)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnVid).setOnClickListener {

            val videoUri = Uri.parse(
                "android.resource://$packageName/${moves[currentPosition].videoRes}"
            )

            tvPreview.setVideoURI(videoUri)

            val mediaController = MediaController(this)
            mediaController.setAnchorView(tvPreview)
            tvPreview.setMediaController(mediaController)

            if( tvPreview.visibility == View.GONE ){
                tvPreview.visibility = View.VISIBLE
                tvPreview.requestFocus()
                tvPreview.start()
            } else {
                tvPreview.visibility = View.GONE
            }
        }
    }

    private fun setupRecycler() {

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.layoutManager = layoutManager

        val adapter = CarouselAdapter(moves) { move ->

            if (currentMove == move) {
                tvDescription.visibility = View.GONE
                currentMove = null
            } else {
                tvDescription.text = move.description
                tvDescription.visibility = View.VISIBLE
                currentMove = move
            }
        }

        recyclerView.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager)
                    val pos = layoutManager.getPosition(centerView!!)
                    if (pos != currentPosition) {
                        currentPosition = pos
                        updateUI(pos)

                        tvDescription.visibility = View.GONE
                        currentMove = null
                    }
                }
            }
        })
    }

    private fun setupDots() {
        layoutDots.removeAllViews()

        for (i in moves.indices) {
            val dot = View(this)
            val params = LinearLayout.LayoutParams(16, 16)
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(R.drawable.dot_inactive)
            layoutDots.addView(dot)
        }
    }

    private fun updateUI(position: Int) {
        tvMoveTitle.text = moves[position].name

        for (i in 0 until layoutDots.childCount) {
            val dot = layoutDots.getChildAt(i)
            dot.setBackgroundResource(
                if (i == position) R.drawable.dot_active
                else R.drawable.dot_inactive
            )
        }

        tvPreview.stopPlayback()
        tvPreview.visibility = View.GONE
    }
}

