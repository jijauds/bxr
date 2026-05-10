package com.bxr.trainingapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.LogRepository
import com.bxr.trainingapp.model.Move

class MoveAdapter(
    private val moves: List<Move>,
    private val onClick: (Move) -> Unit
) : RecyclerView.Adapter<MoveAdapter.MoveViewHolder>() {


    class MoveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvMoveName)
        val accuracy: TextView = view.findViewById(R.id.tvAccuracy)
        val description: TextView = view.findViewById(R.id.tvDescription)
        val image: ImageView = view.findViewById(R.id.imgMove)
        val card: View = view.findViewById(R.id.move_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_move, parent, false)
        return MoveViewHolder(view)
    }

    override fun getItemCount() = moves.size

    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val move = moves[position]

        holder.name.text = move.name
        holder.description.text = move.description
        holder.image.setImageResource(move.imageRes)

        val best = LogRepository.getBestScoreForMove(
            holder.itemView.context,
            move.name
        )

        if (move.name == "Train" || move.name == "Learn" || move.name == "Logs") {

            holder.accuracy.visibility = View.GONE
            holder.image.visibility = View.GONE

            val params = holder.card.layoutParams
            params.height = dpToPx(200, holder.itemView.context)
            holder.card.layoutParams = params

        } else {

            holder.accuracy.visibility = View.VISIBLE
            holder.image.visibility = View.VISIBLE

            val params = holder.card.layoutParams
            params.height = dpToPx(260, holder.itemView.context)
            holder.card.layoutParams = params
            holder.accuracy.text = "Best: $best%"
        }

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