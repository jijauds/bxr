package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.CarouselAdapter
import com.bxr.trainingapp.model.Move
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.PagerSnapHelper

class TrainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvMoveTitle: TextView
    private lateinit var layoutDots: LinearLayout

    private lateinit var moves: List<Move>
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.moves_list)

        recyclerView = findViewById(R.id.recyclerMoves)
        tvMoveTitle = findViewById(R.id.tvMoveTitle)
        layoutDots = findViewById(R.id.layoutDots)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        moves = listOf(
            Move("Jab", R.drawable.move),
            Move("Cross", R.drawable.move),
            Move("Hook", R.drawable.move),
            Move("Uppercut", R.drawable.move)
        )

        setupRecycler()
        setupDots()
        updateUI(0)

        findViewById<Button>(R.id.btnTrain).setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("MOVE_NAME", moves[currentPosition].name)
            startActivity(intent)
        }
    }

    private fun setupRecycler() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CarouselAdapter(moves) {}

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
    }
}
