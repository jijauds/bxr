package com.bxr.trainingapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.data.SessionLog
import com.bxr.trainingapp.ui.VideoPlayerActivity

class LogAdapter(private val logs: List<SessionLog>) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    private var expandedPosition = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvSession: TextView = view.findViewById(R.id.tvSession)
        val tvHandedness: TextView = view.findViewById(R.id.tvHandedness)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)
        val tvReps: TextView = view.findViewById(R.id.tvReps)
        val tvAccuracy: TextView = view.findViewById(R.id.tvAccuracy)
        val details: TextView = view.findViewById(R.id.tvDetails)
        val btnPlayback: Button = view.findViewById(R.id.btnPlayback)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = logs.size

    fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", mins, secs)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val log = logs[position]

        val accuracy = if (log.reps.total > 0) {
            (log.reps.correct.toDouble() / log.reps.total * 100)
        } else 0.0

        // holder.tvDate.text = log.date
        holder.tvSession.text = "Session ${log.id}"
        holder.tvHandedness.text = log.handedness

        holder.tvDuration.text = formatTime(log.duration)
        holder.tvReps.text = "${log.reps.total} Reps"
        holder.tvAccuracy.text = String.format("%.2f%%", accuracy)

        holder.details.text = buildString {
            log.repResults.forEach { rep ->
                append("Rep ${rep.repNumber}  ")

                if (rep.errors.isEmpty()) {
                    append("Perfect form\n")
                } else {
                    append(rep.errors.distinct().joinToString(", "))
                    append("\n")
                }
            }
        }

        val isExpanded = position == expandedPosition

        holder.details.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.btnPlayback.visibility = if (isExpanded) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            // expandedPosition = if (isExpanded) -1 else position
            // notifyDataSetChanged()

            val previous = expandedPosition
            expandedPosition = if (isExpanded) -1 else position

            if (previous != -1) notifyItemChanged(previous)
            notifyItemChanged(position)
        }

        holder.btnPlayback.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("SESSION_ID", log.id)
            context.startActivity(intent)
        }
    }
}