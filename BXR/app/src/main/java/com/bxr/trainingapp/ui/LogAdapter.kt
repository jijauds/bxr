package com.bxr.trainingapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.SessionLog

class LogAdapter(private val logs: List<SessionLog>) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    private var expandedPosition = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val summary: TextView = view.findViewById(R.id.tvSummary)
        val details: TextView = view.findViewById(R.id.tvDetails)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.log_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = logs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val log = logs[position]
        val accuracy = if (log.reps.total > 0) {
            (log.reps.correct.toDouble() / log.reps.total * 100).toInt()
        } else 0

        holder.summary.text =
            buildString {
                append("Session ")
                append(log.id)
                append(" • ")
                append(log.duration)
                append("s • ")
                append(log.reps.correct)
                append("/")
                append(log.reps.total)
                append(" • ")
                append(" ($accuracy%)")
            }

        holder.details.text =
            buildString {
                append("Handedness: ")
                append(log.handedness)
                append("\nErrors:\n")
                append(log.errors.distinct().joinToString("\n"))
            }

        val isExpanded = position == expandedPosition
        holder.details.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {

            expandedPosition =
                if (isExpanded) -1 else position

            notifyDataSetChanged()
        }
    }
}