package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormTracker

private val formAngles = mapOf(
    "L_Hand" to 5,
    "R_Hand" to 170,
    "L_Elbow" to 165,
    "R_Elbow" to 35,
    "L_Knee" to 170,
    "R_Knee" to 170,
    "L_Shoulder" to 90,
    "R_Shoulder" to 5,
    "L_Hip" to 100,
    "R_Hip" to 110
)

fun trackJab(angles: AngleType, formState: FormTracker): FormTracker {
    val angles = angles.angles

    return formState
}