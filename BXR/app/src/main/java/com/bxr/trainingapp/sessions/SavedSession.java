package com.bxr.trainingapp.sessions;

data class SavedSession(
        val moveName: String,
        val startTime: Instant,
        val endTime: Instant,
        val totalReps: Int,
        val totalErrors: Int,
        val accuracy: Float,
        val handedness: Handedness
)