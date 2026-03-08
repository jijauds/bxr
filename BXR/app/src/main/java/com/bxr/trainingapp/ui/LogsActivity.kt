package com.bxr.trainingapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.adapter.CarouselAdapter
import com.bxr.trainingapp.model.Move

class LogsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutDots: LinearLayout
    private lateinit var tvMoveTitle: TextView

    private lateinit var moves: List<Move>

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_logs)

        recyclerView = findViewById(R.id.recyclerMoves)
        layoutDots = findViewById(R.id.layoutDots)
        tvMoveTitle = findViewById(R.id.tvMoveTitle)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        moves = listOf(
            Move("Jab", R.drawable.move, "", 0),
            Move("Straight", R.drawable.move, "", 0),
            Move("Front Hook", R.drawable.move, "", 0),
            Move("Front Uppercut", R.drawable.move, "", 0),
            Move("Rear Uppercut", R.drawable.move, "", 0)
        )

        setupRecycler()
        setupDots()
        updateUI(0)

        findViewById<Button>(R.id.btnView).setOnClickListener {

            val intent = Intent(this, LogListActivity::class.java)

            intent.putExtra(
                "PUNCH_TYPE",
                moves[currentPosition].name
            )

            startActivity(intent)
        }
    }

    private fun setupRecycler() {

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.layoutManager = layoutManager

        val adapter = CarouselAdapter(moves) { }

        recyclerView.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {

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
                if (i == position)
                    R.drawable.dot_active
                else
                    R.drawable.dot_inactive
            )
        }
    }
}