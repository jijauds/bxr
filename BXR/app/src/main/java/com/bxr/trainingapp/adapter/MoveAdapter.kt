package com.bxr.trainingapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.Move

class MoveAdapter(
    private val moves: List<Move>,
    private val onClick: (Move) -> Unit
) : RecyclerView.Adapter<MoveAdapter.MoveViewHolder>() {

    class MoveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvMoveName)
        val accuracy: TextView = view.findViewById(R.id.tvAccuracy)
        val description: TextView = view.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_move, parent, false)
        return MoveViewHolder(view)
    }

    override fun getItemCount() = moves.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val move = moves[position]

        holder.name.text = move.name
        holder.description.text = move.description
        holder.accuracy.text = "${(85..99).random()}%"

        holder.itemView.setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .scaleX(0.96f)
                        .scaleY(0.96f)
                        .setDuration(120)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start()

                    // v.performClick()
                }

                MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start()
                }
            }

            false
        }

        holder.itemView.setOnClickListener {
            onClick(move)
        }
    }
}