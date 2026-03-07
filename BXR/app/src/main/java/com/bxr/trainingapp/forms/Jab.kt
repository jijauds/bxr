package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker

private val jabAngles = mapOf(
    "L_Hand" to 170.0,
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

private const val THRESHOLD = 20.0

fun trackJab(angleType: AngleType, tracker: FormTracker): FormTracker {

    val angles = angleType.angles
    Log.d("REPS", tracker.reps.toString())
    val checkError = GenericErrorChecker()
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
                    tracker.errors += "Starting position"
                    tracker.state = FormStates.inProgress
                }
            }
        }

        FormStates.inProgress -> {
            val checkJab = checkAngle(angles, jabAngles, THRESHOLD)
            Log.d("JABERRORS", checkJab.errors.toString())
            tracker.addKeyPoseErrors(checkJab.errors)
            tracker.changeKeypoints(checkJab.keypoints)

            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.guardHandCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.errors += "Guard hand goes down"
                    tracker.currentErrors += "Guard hand goes down"
                }
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles)) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.errors += "Punch not straight"
                    tracker.currentErrors += "Punch not straight"
                }
            }

            //Check if leaning
            //Check if leaning forward
            if (checkError.leanForwardCheck(angles)) {
                tracker.errorCounter.leaningForward++
                if (tracker.errorCounter.leaningForward > errorFrameCheck) {
                    tracker.errorCounter.leaningForward = 0
                    tracker.errors += "Leaning forward"
                    tracker.currentErrors += "Leaning forward"
                }
            }
            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.errors += "Leaning backwards"
                    tracker.currentErrors += "Leaning backwards"
                }
            }

            // Check if punch was stretched out
            if (angles["L_Elbow"]!!.angle in 160.0..200.0) {
                tracker.errorCounter.punchNotFull = false
            }

            if (angles["L_Hand"]!!.x < tracker.errorCounter.handX) {
                tracker.errorCounter.punchNotFullCounter++
                if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                    tracker.errorCounter.punchNotFullCounter = 0
                    if (tracker.errorCounter.punchNotFull) {
                        tracker.errors += "Punch not full"
                        tracker.currentErrors += "Punch not full"
                    }
                    tracker.errorCounter.punchNotFull = true
                }
            }
            val atClimax = checkJab.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            Log.d("GUARDERRORS", checkGuard.errors.toString())
            tracker.addKeyPoseErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.guardHandCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.errors += "Guard hand goes down"
                    tracker.currentErrors += "Guard hand goes down"
                }
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles)) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.errors += "Punch not straight"
                    tracker.currentErrors += "Punch not straight"
                }
            }

            //Check if leaning
            //Check if leaning forward
            if (checkError.leanForwardCheck(angles)) {
                tracker.errorCounter.leaningForward++
                if (tracker.errorCounter.leaningForward > errorFrameCheck) {
                    tracker.errorCounter.leaningForward = 0
                    tracker.errors += "Leaning forward"
                    tracker.currentErrors += "Leaning forward"
                }
            }
            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.errors += "Leaning backwards"
                    tracker.currentErrors += "Leaning backwards"
                }
            }
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.state = FormStates.notStarted
                tracker.addReps(Triple(1, 0, 0))
            }
        }
    }

    return tracker
}