package com.bxr.trainingapp.model

import android.content.Context
import com.bxr.trainingapp.data.LogRepository

data class Move(
    val name: String,
    val imageRes: Int,
    val description: String,
    val criticalPoints: List<String>,
    val videoRes: Int
)

sealed class HowToContent {
    data class Text(val text: String) : HowToContent()
    data class Subtitle(val text: String) : HowToContent()
    data class Image(val resId: Int) : HowToContent()
}

data class HowToItem(
    val title: String,
    val content: List<HowToContent>
)

object moveScore {

    var bestScores: Map<String, Int> = emptyMap()
        private set

    fun refresh(context: Context) {
        val logs = LogRepository.loadLogs(context)

        bestScores = logs
            .groupBy { it.punchType }
            .mapValues { entry ->
                entry.value.maxOfOrNull { log ->
                    val total = log.reps.total
                    val correct = log.reps.correct

                    if (total > 0) (correct * 100) / total else 0
                } ?: 0
            }
    }
}
