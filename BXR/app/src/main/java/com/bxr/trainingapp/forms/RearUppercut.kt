package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Reps

private val rearUpperCutAngles = mapOf(
    //"L_Hand" to 170.0, Occluded
    "R_Hand" to 147.0,
    //"L_Elbow" to 165.0,
    "R_Elbow" to 75.0,
    "L_Knee" to 160.0,
    "R_Knee" to 145.0,
    //"L_Shoulder" to 90.0,
    "R_Shoulder" to 67.0,
    "L_Hip" to 50.0,
    "R_Hip" to 83.0
)

private const val THRESHOLD = 25.0
private val checkError = GenericErrorChecker()

fun trackRearUpperCut(angleType: AngleType, tracker: FormTracker): FormTracker {
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
            val checkRearUpperCut = checkRearUpperCut(angles, rearUpperCutAngles, 0.05)
            if (angles["R_Hand"] != null){
                tracker.errorCounter.handX = angles["R_Hand"]!!.x
            }

            // tracker.currentErrors.addAll(checkJab.errors)
            tracker.addKeyPoseErrors(checkRearUpperCut.errors)
            tracker.changeKeypoints(checkRearUpperCut.keypoints)

            //Check if hands are wrong
            //Check lead hand placement -- OCCLUDED

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Rear Upper Cut")) {
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
            //Check if leaning backward
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
            if (angles["R_Hand"]!!.y < angles["R_Shoulder"]!!.y+0.05) {
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
            val atClimax = checkRearUpperCut.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            tracker.currentErrors.addAll(checkGuard.errors)
            tracker.addKeyPoseErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            //Check if hands are wrong
            //Check lead hand placement -- OCCLUDED

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Rear Upper Cut")) {
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
            //Check if leaning backward

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