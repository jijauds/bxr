package com.bxr.trainingapp.model

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

