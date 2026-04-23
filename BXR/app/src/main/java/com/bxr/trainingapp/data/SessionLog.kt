package com.bxr.trainingapp.data

import com.bxr.trainingapp.sessions.RepResult
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

data class SessionLog(
    val id: Int,
    val punchType: String,
    val duration: Int,
    val handedness: String,
    val reps: Reps,
    val repResults: List<RepResult>,
    val date: String ?= null
)

data class Reps(
    val correct: Int,
    val wrong: Int,
    val total: Int
)