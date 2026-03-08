package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Reps

private val straightAngles = mapOf(
    //"L_Hand" to 170.0, Occluded
    //"R_Hand" to 170.0,
    //"L_Elbow" to 165.0,
    "R_Elbow" to 165.0,
    "L_Knee" to 160.0,
    "R_Knee" to 135.0,
    //"L_Shoulder" to 90.0,
    "R_Shoulder" to 105.0,
    "L_Hip" to 60.0,
    "R_Hip" to 85.0
)

private const val THRESHOLD = 25.0
private val checkError = GenericErrorChecker()

fun trackStraight(angleType: AngleType, tracker: FormTracker): FormTracker {
    val angles = angleType.angles

    // Log.d("REPS", tracker.reps.toString())
    val errorFrameCheck = 2

    when (tracker.state) {
        FormStates.notStarted -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
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
            val checkStraight = checkStraight(angles, straightAngles, THRESHOLD)
            if (angles["R_Hand"] != null){
                tracker.errorCounter.handX = angles["R_Hand"]!!.x
            }

            // tracker.currentErrors.addAll(checkJab.errors)
            tracker.addKeyPoseErrors(checkStraight.errors)
            tracker.changeKeypoints(checkStraight.keypoints)

            //Check if hands are wrong
            //Check lead hand placement -- OCCLUDED

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Straight")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.wasWrong = true
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.errorsWithDuplicates.add("Punch not straight")
                    tracker.currentErrors.add("Punch not straight")
                }
            } else {
                tracker.errorCounter.punchNotStraight = 0
            }

            //Check if leaning
            //Check if leaning forward
            if (checkError.leanForwardCheck(angles)) {
                tracker.errorCounter.leaningForward++
                if (tracker.errorCounter.leaningForward > errorFrameCheck) {
                    tracker.wasWrong = true
                    tracker.errorCounter.leaningForward = 0
                    tracker.errorsWithDuplicates.add("Leaning forward")
                    tracker.currentErrors.add("Leaning forward")
                }
            } else {
                tracker.errorCounter.leaningForward = 0
            }
            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.wasWrong = true
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.errorsWithDuplicates.add("Leaning backwards")
                    tracker.currentErrors.add("Leaning backwards")
                }
            } else {
                tracker.errorCounter.leaningBackwards = 0
            }

            // Check if punch was stretched out
            if (angles["R_Elbow"]!!.angle in 155.0..180.0) {
                tracker.errorCounter.punchNotFull = false
                tracker.errorCounter.punchNotFullCounter = 0
            }
            if (angles["R_Hand"]!!.x < tracker.errorCounter.handX) {
                tracker.errorCounter.punchNotFullCounter++
                if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                    if (tracker.errorCounter.punchNotFull) {
                        tracker.wasWrong = true
                        tracker.errorsWithDuplicates.add("Punch not full")
                        tracker.currentErrors.add("Punch not full")
                    }
                    tracker.errorCounter.punchNotFull = true
                }
            }
            val atClimax = checkStraight.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            Log.d("GUARDERRORS", checkGuard.errors.toString())
            tracker.currentErrors.addAll(checkGuard.errors)
            tracker.addKeyPoseErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            //Check if hands are wrong
            //Check lead hand placement -- OCCLUDED

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Straight")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.wasWrong = true
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.errorsWithDuplicates.add("Punch not straight")
                    tracker.currentErrors.add("Punch not straight")
                }
            } else {
                tracker.errorCounter.punchNotStraight = 0
            }

            //Check if leaning
            //Check if leaning forward
            if (checkError.leanForwardCheck(angles)) {
                tracker.errorCounter.leaningForward++
                if (tracker.errorCounter.leaningForward > errorFrameCheck) {
                    tracker.wasWrong = true
                    tracker.errorCounter.leaningForward = 0
                    tracker.errorsWithDuplicates.add("Leaning forward")
                    tracker.currentErrors.add("Leaning forward")
                }
            } else {
                tracker.errorCounter.leaningForward = 0
            }

            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.wasWrong = true
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.errorsWithDuplicates.add("Leaning backwards")
                    tracker.currentErrors.add("Leaning backwards")
                }
            } else {
                tracker.errorCounter.leaningBackwards = 0
            }
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.addErrors(tracker.errorsWithDuplicates.toSet().toList())
                tracker.errorsWithDuplicates = mutableListOf()
                tracker.state = FormStates.notStarted
                tracker.errorCounter.reset()
                val repCount = Reps(1,0, 0)
                if(tracker.wasWrong) {
                    repCount.wrong++
                } else {
                    repCount.correct++
                }
                tracker.wasWrong = false
                tracker.addReps(repCount)
            }
        }
    }

    return tracker
}
