package com.bxr.trainingapp.model

data class SessionLog(
    val id: Int,
    val punchType: String,
    val duration: Int,
    val handedness: String,
    val errors: List<String>,
    val reps: Reps
)

data class Reps(
    val correct: Int,
    val wrong: Int,
    val total: Int
)