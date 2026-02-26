package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker

private val jabAngles = mapOf(
    "L_Hand" to 5.0,
    //"R_Hand" to 170.0,
    "L_Elbow" to 165.0,
    "R_Elbow" to 35.0,
    "L_Knee" to 170.0,
    "R_Knee" to 170.0,
    "L_Shoulder" to 90.0,
    "R_Shoulder" to 5.0,
    "L_Hip" to 100.0,
    "R_Hip" to 111.0
)

private const val THRESHOLD = 20.0

fun trackJab(angleType: AngleType, tracker: FormTracker): FormTracker {

    val angles = angleType.angles
    Log.d("REPS", tracker.reps.toString())

    when (tracker.state) {
        FormStates.notStarted -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            Log.d("GUARDERRORS", checkGuard.errors.toString())
            tracker.addErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.state = FormStates.inProgress
            }
        }

        FormStates.inProgress -> {
            val checkJab = checkAngle(angles, jabAngles, THRESHOLD)
            Log.d("JABERRORS", checkJab.errors.toString())
            tracker.addErrors(checkJab.errors)
            tracker.changeKeypoints(checkJab.keypoints)
            val atClimax = checkJab.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            Log.d("GUARDERRORS", checkGuard.errors.toString())
            tracker.addErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.state = FormStates.notStarted
                tracker.addReps(Triple(1, 0, 0))
            }
        }
    }

    return tracker
}