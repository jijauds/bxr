package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Reps

private val leadUpperCutAngles = mapOf(
    "L_Hand" to 140.0,
    "R_Hand" to 55.0,
    "L_Elbow" to 60.0,
    "R_Elbow" to 35.0,
    "L_Knee" to 172.0,
    "R_Knee" to 175.0,
    "L_Shoulder" to 63.0,
    "R_Shoulder" to 11.0,
    "L_Hip" to 122.0,
    "R_Hip" to 101.0
)

private const val THRESHOLD = 25.0
private val checkError = GenericErrorChecker()

fun trackLeadUpperCut(angleType: AngleType, tracker: FormTracker): FormTracker {
    val angles = angleType.angles

    // Log.d("REPS", tracker.reps.toString())
    val errorFrameCheck = 2

    when (tracker.state) {
        FormStates.notStarted -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            Log.d("GUARDERRORS", checkGuard.errors.toString())
            tracker.addKeyPoseErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            tracker.currentErrors = checkGuard.errors
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.errorCounter.startingPosition++
                if (tracker.errorCounter.startingPosition > errorFrameCheck) {
                    tracker.errorCounter.startingPosition = 0
                    tracker.errorCounter.handX = angles["L_Hand"]!!.x
                    tracker.state = FormStates.inProgress
                }
            }
        }

        FormStates.inProgress -> {
            val checkLeadUpperCut = checkLeadUpperCutAngle(angles, leadUpperCutAngles, THRESHOLD)
            if (angles["L_Hand"] != null){
                tracker.errorCounter.handX = angles["L_Hand"]!!.x
            }

            // tracker.currentErrors.addAll(checkLeadUpperCut.errors)
            tracker.addKeyPoseErrors(checkLeadUpperCut.errors)
            tracker.changeKeypoints(checkLeadUpperCut.keypoints)

            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.leadUpperCutGuardCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.addErrors(listOf("Guard hand goes down"))
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.currentErrors.add("Guard hand goes down")
                }
            } else {
                tracker.errorCounter.guardHandGoesDown = 0
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Lead Upper Cut")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                }
            } else {
                tracker.errorCounter.punchNotStraight = 0
            }

            //Check if leaning
            //Check if leaning backward
            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.addErrors(listOf("Leaning backwards"))
                    tracker.currentErrors.add("Leaning backwards")
                }
            } else {
                tracker.errorCounter.leaningBackwards = 0
            }

            // Check if punch was stretched out
            if (angles["L_Hand"]!!.y < angles["L_Shoulder"]!!.y+0.05) {
                tracker.errorCounter.punchNotFull = false
                tracker.errorCounter.punchNotFullCounter = 0
            }
            if (angles["L_Hand"]!!.x < tracker.errorCounter.handX) {
                tracker.errorCounter.punchNotFullCounter++
                if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                    if (tracker.errorCounter.punchNotFull) {
                        tracker.addErrors(listOf("Punch not full"))
                        tracker.currentErrors.add("Punch not full")
                        tracker.errorCounter.punchNotFull = true
                    }
                    tracker.errorCounter.punchNotFull = true
                }
            }
            val atClimax = checkLeadUpperCut.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            Log.d("GUARDERRORS", checkGuard.errors.toString())
//            tracker.currentErrors.addAll(checkGuard.errors)
//            tracker.addKeyPoseErrors(checkGuard.errors)
//            tracker.changeKeypoints(checkGuard.keypoints)
            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.leadUpperCutGuardCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.addErrors(listOf("Guard hand goes down"))
                    tracker.currentErrors.add("Guard hand goes down")
                }
            } else {
                tracker.errorCounter.guardHandGoesDown = 0
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Lead Upper Cut")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                }
            } else {
                tracker.errorCounter.punchNotStraight = 0
            }

            //Check if leaning
            //Check if leaning backward

            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.addErrors(listOf("Leaning backwards"))
                    tracker.currentErrors.add("Leaning backwards")
                }
            } else {
                tracker.errorCounter.leaningBackwards = 0
            }
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.state = FormStates.notStarted
                tracker.errorCounter.reset()
                tracker.wasWrong = false
            }
        }
    }

    return tracker
}
