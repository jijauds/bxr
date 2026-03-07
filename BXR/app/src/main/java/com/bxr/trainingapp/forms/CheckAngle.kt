package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.Coords

data class AngleResults(
    val keypoints: Map<String, Boolean>,
    val errors: MutableList<String>
)
fun checkAngle(angles: Map<String, Coords>, correctangles: Map<String, Double>, threshhold: Double): AngleResults{
    Log.d("CORRECTANGLE", correctangles.toString())
    val errors = mutableListOf<String>()
    val keypoints = mutableMapOf<String, Boolean>()
    for ((name, angle) in angles) {
        if ((angle.angle < correctangles[name]!! - threshhold || angle.angle > correctangles[name]!! + threshhold)) {
            errors.add("$name is wrong")
            keypoints[name] = false
        } else {
            keypoints[name] = true
        }
    }
    if (angles["L_KNEE"]!!.x < angles["R_KNEE"]!!.x) {
        errors.add("")
        keypoints["L_KNEE"] = false
    }
    return AngleResults(keypoints, errors)
}

