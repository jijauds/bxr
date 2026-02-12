package com.bxr.trainingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.Move

class CarouselAdapter(
    private val moves: List<Move>,
    private val onClick: (Move) -> Unit
) : RecyclerView.Adapter<CarouselAdapter.MoveViewHolder>() {

    inner class MoveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMove: ImageView = view.findViewById(R.id.imgMove)
        val tvMoveName: TextView = view.findViewById(R.id.tvMoveName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_move, parent, false)
        return MoveViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val move = moves[position]
        holder.tvMoveName.text = move.name
        holder.imgMove.setImageResource(move.imageRes)

        holder.itemView.setOnClickListener {
            onClick(move)
        }
    }

    override fun getItemCount(): Int = moves.size
}
