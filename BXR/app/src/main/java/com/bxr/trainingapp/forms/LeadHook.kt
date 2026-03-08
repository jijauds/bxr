package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Reps

private val leadHookAngles = mapOf(
    "L_Hand" to 170.0,
    //"R_Hand" to 170.0,
    //"L_Elbow" to 165.0,
    "R_Elbow" to 35.0,
    "L_Knee" to 172.0,
    "R_Knee" to 175.0,
    //"L_Shoulder" to 90.0,
    "R_Shoulder" to 8.0,
    "L_Hip" to 106.0,
    "R_Hip" to 115.0
)

private const val THRESHOLD = 25.0
private val checkError = GenericErrorChecker()

fun trackLeadHook(angleType: AngleType, tracker: FormTracker): FormTracker {
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
            val checkLeadHook = checkLeadHook(angles, leadHookAngles, THRESHOLD)
            // For Lead Hook, check elbow
            if (angles["L_Elbow"] != null){
                tracker.errorCounter.handX = angles["L_Elbow"]!!.y
            }

            // tracker.currentErrors.addAll(checkJab.errors)
            tracker.addKeyPoseErrors(checkLeadHook.errors)
            tracker.changeKeypoints(checkLeadHook.keypoints)

            //Check if hands are wrong
            //Check lead hand placement -- OCCLUDED

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Lead Hook")) {
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
            if (angles["L_Hand"]?.x != null && angles["L_Elbow"]?.x != null && angles["L_Elbow"]?.x != null) {
                if (angles["L_Hand"]!!.x in angles["L_Elbow"]!!.x-0.05..angles["L_Elbow"]!!.x+0.05 && angles["L_Hand"]!!.y in angles["L_Elbow"]!!.y-0.05..angles["L_Elbow"]!!.y+0.05 ) {
                    tracker.errorCounter.punchNotFull = false
                    tracker.errorCounter.punchNotFullCounter = 0
                }
            }
            if (angles["L_Elbow"]!!.y > tracker.errorCounter.handX) {
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
            val atClimax = checkLeadHook.errors.isEmpty()
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
            if (checkError.punchStraightCheck(angles, "Lead Hook")) {
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
