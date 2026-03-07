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
        Log.d("ANGLECHECKER", "$name: $angle")
        if (name == "R_Hand") {
            if (angles["R_Shoulder"]?.x != null && angles["L_Shoulder"]?.x != null) {
                if (angle.x in angles["R_Shoulder"]!!.x..angles["L_Shoulder"]!!.x && angle.y in angles["R_Shoulder"]!!.y - 20..angles["R_Shoulder"]!!.y + 20) {
                    keypoints[name] = true
                } else {
                    keypoints[name] = false
                    errors.add("$name is wrong")
                }
                continue
            } else {
                keypoints[name] = false
                errors.add("$name is wrong")
                continue
            }
        }
        if ((angle.angle < correctangles[name]!! - threshhold || angle.angle > correctangles[name]!! + threshhold)) {
            errors.add("$name is wrong")
            keypoints[name] = false
        } else {
            keypoints[name] = true
        }
    }
    if (angles["L_Knee"]?.x != null && angles["R_Knee"]?.x != null){
        if (angles["L_Knee"]!!.x < angles["R_Knee"]!!.x) {
            errors.add("Wrong foot forward")
            keypoints["L_Knee"] = false
            keypoints["R_Knee"] = false
        }
    }
    return AngleResults(keypoints, errors)
}

