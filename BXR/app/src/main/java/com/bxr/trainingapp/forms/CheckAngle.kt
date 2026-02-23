package com.bxr.trainingapp.forms

import android.util.Log

data class AngleResults(
    val keypoints: Map<String, Boolean>,
    val errors: MutableList<String>
)
fun checkAngle(angles: Map<String, Double>, correctangles: Map<String, Double>, threshhold: Double): AngleResults{
    Log.d("CORRECTANGLE", correctangles.toString())
    val errors = mutableListOf<String>()
    val keypoints = mutableMapOf<String, Boolean>()
    for ((name, angle) in angles) {
        if ((angle < correctangles[name]!! - threshhold || angle > correctangles[name]!! + threshhold)) {
            errors.add("$name is wrong")
            keypoints[name] = false
        } else {
            keypoints[name] = true
        }
    }
    return AngleResults(keypoints, errors)
}

