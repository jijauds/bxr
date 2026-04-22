package com.bxr.trainingapp.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.HowToContent
import com.bxr.trainingapp.model.HowToItem

class HowToAdapter(
    private val items: List<HowToItem>
) : RecyclerView.Adapter<HowToAdapter.ViewHolder>() {

    private var expandedPosition = -1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val contentContainer: LinearLayout = view.findViewById(R.id.contentContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_howtouse, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val isExpanded = position == expandedPosition

        holder.title.text = item.title

        holder.contentContainer.removeAllViews()
        holder.contentContainer.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded) {
            val context = holder.itemView.context
            val density = context.resources.displayMetrics.density
            val margin = (8 * density).toInt()

            item.content.forEach { block ->

                when (block) {

                    is HowToContent.Text -> {
                        val textView = TextView(context).apply {
                            text = block.text
                            textSize = 14f
                            setTextColor(
                                ContextCompat.getColor(context, R.color.white)
                            )
                        }

                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            topMargin = margin
                            bottomMargin = margin
                        }

                        textView.layoutParams = params
                        holder.contentContainer.addView(textView)
                    }

                    is HowToContent.Subtitle -> {
                        val textView = TextView(context).apply {
                            text = block.text
                            textSize = 15f
                            setTypeface(null, Typeface.BOLD)
                            setTextColor(
                                ContextCompat.getColor(context, R.color.accent)
                            )
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }

                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            topMargin = margin
                            bottomMargin = margin / 2
                        }

                        textView.layoutParams = params
                        holder.contentContainer.addView(textView)
                    }

                    is HowToContent.Image -> {
                        val imageView = ImageView(context).apply {
                            setImageResource(block.resId)
                            scaleType = ImageView.ScaleType.CENTER_INSIDE
                        }

                        val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            (200 * density).toInt(),
                            0.7f
                        ).apply {
                            topMargin = margin
                            bottomMargin = margin
                        }

                        imageView.layoutParams = params
                        holder.contentContainer.addView(imageView)
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            val previous = expandedPosition
            expandedPosition = if (isExpanded) -1 else position

            if (previous != -1) notifyItemChanged(previous)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size
}