package com.bxr.trainingapp.data

import com.bxr.trainingapp.sessions.RepResult

data class SessionLog(
    val id: Int,
    val punchType: String,
    val duration: Int,
    val handedness: String,
    val reps: Reps,
    val repResults: List<RepResult>
)

data class Reps(
    val correct: Int,
    val wrong: Int,
    val total: Int
)