package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker

private val jabAngles = mapOf(
    "L_Hand" to 5.0,
    "R_Hand" to 170.0,
    "L_Elbow" to 165.0,
    "R_Elbow" to 35.0,
    "L_Knee" to 170.0,
    "R_Knee" to 170.0,
    "L_Shoulder" to 90.0,
    "R_Shoulder" to 5.0,
    "L_Hip" to 100.0,
    "R_Hip" to 111.0
)

private const val THRESHOLD = 20

fun trackJab(angleType: AngleType, tracker: FormTracker): FormTracker {

    val angles = angleType.angles
    val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
    val atGuard = checkGuard.errors.isEmpty()
    val checkJab = checkAngle(angles, jabAngles, THRESHOLD)
    val atClimax = checkJab.errors.isEmpty()

    when (tracker.state) {
        FormStates.notStarted -> {
            if (atGuard) {
                tracker.state = FormStates.inProgress
            }
        }

        FormStates.inProgress -> {
            if (atClimax) {
                tracker.state = FormStates.completed
            }
        }

        FormStates.completed -> {
            if (atGuard) {
                tracker.state = FormStates.notStarted
                tracker.addReps(Triple(1, 0, 0))
            }
        }
    }

    return tracker
}