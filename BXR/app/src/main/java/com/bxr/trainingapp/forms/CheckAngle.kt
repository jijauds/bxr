package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.Coords

data class AngleResults(
    val keypoints: Map<String, Boolean>,
    val errors: MutableList<String>
)

val errorMessages = mapOf(
    "R_Hand" to "Place your right hand between your shoulders",
    "L_Knee" to "Move your left leg forward",
    "R_Knee" to "Move your right leg back",
    "R_Elbow" to "Fix your right arm",
    "L_Elbow" to "Fix your left arm",
    "L_Shoulder" to "Fix your left arm",
    "R_Shoulder" to "Fix your right arm",
    "L_Hip" to "Fix your left leg",
    "R_Hip" to "Fix your right leg"
)
fun getError(name: String, errorMessages: Map<String, String>): String {
    return errorMessages[name] ?: "$name position is incorrect"
}

fun checkAngle(angles: Map<String, Coords>, correctangles: Map<String, Double>, threshhold: Double): AngleResults{

    Log.d("CORRECTANGLE", correctangles.toString())

    val errors = mutableListOf<String>()
    val keypoints = mutableMapOf<String, Boolean>()

    for ((name, angle) in angles) {

        Log.d("ANGLECHECKER", "$name: $angle")

        if (name == "R_Hand") {

            if (angles["R_Shoulder"]?.x != null && angles["L_Shoulder"]?.x != null) {
                if (angle.x in angles["R_Shoulder"]!!.x..angles["L_Shoulder"]!!.x && angle.y in angles["R_Shoulder"]!!.y - 0.05..angles["R_Shoulder"]!!.y + 0.05) {
                    keypoints[name] = true
                } else {
                    keypoints[name] = false
                    errors.add(getError(name, errorMessages))
                }
                continue
            } else {
                keypoints[name] = false
                errors.add(getError(name, errorMessages))
                continue
            }
        }
        if ((angle.angle < correctangles[name]!! - threshhold || angle.angle > correctangles[name]!! + threshhold)) {
            keypoints[name] = false
            errors.add(getError(name, errorMessages))
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

