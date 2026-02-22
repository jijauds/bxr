package com.bxr.trainingapp.data

import kotlin.text.set
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

data class AngleType (
    var angles: MutableMap<String, Double>
)

class Angles {
    private val triplets = mapOf(
        "L_Hand" to Triple(11,15,12),
        "R_Hand" to Triple(12,16,11),
        "L_Elbow" to Triple(11,13,15),
        "R_Elbow" to Triple(12,14,16),
        "L_Knee" to Triple(23,25,27),
        "R_Knee" to Triple(24,26,28),
        "L_Shoulder" to Triple(13,11,23),
        "R_Shoulder" to Triple(14,12,24),
        "L_Hip" to Triple(24,23,25),
        "R_Hip" to Triple(23,24,26),
    )

    private val angleList = AngleType(mutableMapOf(
        "L_Hand" to 0.0,
        "R_Hand" to 0.0,
        "L_Elbow" to 0.0,
        "R_Elbow" to 0.0,
        "L_Knee" to 0.0,
        "R_Knee" to 0.0,
        "L_Shoulder" to 0.0,
        "R_Shoulder" to 0.0,
        "L_Hip" to 0.0,
        "R_Hip" to 0.0
    ))
    private var results: PoseLandmarkerResult? = null

    public fun getAngles(): AngleType? {
        val result = results ?: return null
        if (result.landmarks().isEmpty()) return null

        val landmarks = result.landmarks()[0]

        triplets.forEach { (name, indices) ->
            val (pointA, pointB, pointC) = indices

            val landmarkA = landmarks[pointA]
            val landmarkB = landmarks[pointB]
            val landmarkC = landmarks[pointC]

            angleList.angles[name] = calculateAngle(
                Pair(landmarkA.x(), landmarkA.y()),
                Pair(landmarkB.x(), landmarkB.y()),
                Pair(landmarkC.x(), landmarkC.y())
            )
        }
        return angleList
    }
}