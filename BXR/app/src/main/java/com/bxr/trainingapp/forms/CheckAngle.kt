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
        if (name == "HEAD") {
            continue
        }
        if (name == "R_HAND") {
            if (angle.x in angles["HEAD"]!!.x - 10..angles["HEAD"]!!.x + 10 && angle.y > angles["R_SHOULDER"]!!.y){
                keypoints[name] = true
            } else {
                keypoints[name] = false
                errors.add("$name is wrong")
            }
            continue
        }
        if ((angle.angle < correctangles[name]!! - threshhold || angle.angle > correctangles[name]!! + threshhold)) {
            errors.add("$name is wrong")
            keypoints[name] = false
        } else {
            keypoints[name] = true
        }
    }
    if (angles["L_KNEE"]?.x != null && angles["R_KNEE"]?.x != null){
        if (angles["L_KNEE"]!!.x < angles["R_KNEE"]!!.x) {
            errors.add("Wrong foot forward")
            keypoints["L_KNEE"] = false
        }
    }
    return AngleResults(keypoints, errors)
}

