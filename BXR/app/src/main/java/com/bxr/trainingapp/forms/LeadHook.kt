package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Reps

private val leadHookAngles = mapOf(
    "L_Hand" to Pair(36.0, 180.0),
    //"R_Hand" to Pair(3.0, 78.0),
    //"L_Elbow" to Pair(1.0, 180.0),
    "R_Elbow" to Pair(26.0, 71.0),
    "L_Knee" to Pair(137.0,180.0),
    "R_Knee" to Pair(130.0,180.0),
    "L_Shoulder" to Pair(65.0,114.0),
    "R_Shoulder" to Pair(0.0,60.0),
    "L_Hip" to Pair(82.0,123.0),
    "R_Hip" to Pair(89.0,117.0)
)

private const val THRESHOLD = 25.0
private val checkError = GenericErrorChecker()

fun trackLeadHook(angleType: AngleType, tracker: FormTracker): FormTracker {
    val angles = angleType.angles

    val errorFrameCheck = 2

    when (tracker.state) {
        FormStates.notStarted -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            tracker.addKeyPoseErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)
            tracker.currentErrors = checkGuard.errors.toMutableList()
            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.errorCounter.startingPosition++
                if (tracker.errorCounter.startingPosition > errorFrameCheck) {
                    tracker.errorCounter.startingPosition = 0
                    angles["L_Elbow"]?.let { tracker.errorCounter.handX = it.y }
                    tracker.state = FormStates.inProgress
                }
            }
        }

        FormStates.inProgress -> {
            val checkLeadHook = checkLeadHook(angles, leadHookAngles, THRESHOLD)
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()
            // For Lead Hook, check elbow

            // tracker.currentErrors.addAll(checkJab.errors)
            tracker.addKeyPoseErrors(checkLeadHook.errors)
            // tracker.changeKeypoints(checkLeadHook.keypoints)

            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.guardHandCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.addErrors(listOf("Guard hand goes down"))
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.currentErrors.add("Guard hand goes down")
                    keypointColors["R_Hand"] = false
                    tracker.wasWrong = true
                }
            } else {
                tracker.errorCounter.guardHandGoesDown = 0
            }

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Lead Hook")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.currentErrors.add("Punch not straight")
                    keypointColors["L_Hand"] = false
                    tracker.wasWrong = true
                }
            } else {
                tracker.errorCounter.punchNotStraight = 0
            }

            //Check if leaning
            //Check if leaning forward
            if (checkError.leanForwardCheck(angles)) {
                tracker.errorCounter.leaningForward++
                if (tracker.errorCounter.leaningForward > errorFrameCheck) {
                    tracker.addErrors(listOf("Leaning forward"))
                    tracker.currentErrors.add("Leaning forward")
                    keypointColors["L_Hip"] = false
                    keypointColors["R_Hip"] = false
                    tracker.wasWrong = true
                }
            } else {
                tracker.errorCounter.leaningForward = 0
            }
            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.addErrors(listOf("Leaning backwards"))
                    tracker.currentErrors.add("Leaning backwards")
                    keypointColors["L_Hip"] = false
                    keypointColors["R_Hip"] = false
                    tracker.wasWrong = true
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
            if (angles["L_Elbow"]!!.y < tracker.errorCounter.handX+0.01) {
                tracker.errorCounter.punchNotFullCounter++
                if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                    if (tracker.errorCounter.punchNotFull) {
                        tracker.addErrors(listOf("Punch not full"))
                        tracker.wasWrong = true
                        tracker.currentErrors.add("Punch not full")
                        tracker.errorCounter.punchNotFull = true
                        keypointColors["L_Elbow"] = false
                    }
                    tracker.errorCounter.punchNotFull = true
                }
            } else {
                tracker.errorCounter.punchNotFullCounter = 0
            }
            if (angles["L_Elbow"] != null){
                tracker.errorCounter.handX = angles["L_Elbow"]!!.y
            }
            val atClimax = checkLeadHook.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
            tracker.changeKeypoints(keypointColors)
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()
//            tracker.currentErrors.addAll(checkGuard.errors)

            checkGuard.errors.forEach { error ->
                if (!tracker.currentErrors.contains(error)) {
                    tracker.currentErrors.add(error)
                }
            }

            tracker.addKeyPoseErrors(checkGuard.errors)
            // tracker.changeKeypoints(checkGuard.keypoints)

            if (checkError.guardHandCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.addErrors(listOf("Guard hand goes down"))
                    tracker.currentErrors.add("Guard hand goes down")
                    keypointColors["R_Hand"] = false
                    tracker.wasWrong = true
                }
            } else {
                tracker.errorCounter.guardHandGoesDown = 0
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Lead Hook")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                    keypointColors["L_Hand"] = false
                    tracker.wasWrong = true
                }
            } else {
                tracker.errorCounter.punchNotStraight = 0
            }

            //Check if leaning
            //Check if leaning forward
            if (checkError.leanForwardCheck(angles)) {
                tracker.errorCounter.leaningForward++
                if (tracker.errorCounter.leaningForward > errorFrameCheck) {
                    tracker.errorCounter.leaningForward = 0
                    tracker.addErrors(listOf("Leaning forward"))
                    tracker.currentErrors.add("Leaning forward")
                    keypointColors["L_Hip"] = false
                    keypointColors["R_Hip"] = false
                    tracker.wasWrong = true
                }
            } else {
                tracker.errorCounter.leaningForward = 0
            }

            if (checkError.leanBackCheck(angles)) {
                tracker.errorCounter.leaningBackwards++
                if (tracker.errorCounter.leaningBackwards > errorFrameCheck) {
                    tracker.errorCounter.leaningBackwards = 0
                    tracker.addErrors(listOf("Leaning backwards"))
                    tracker.currentErrors.add("Leaning backwards")
                    keypointColors["L_Hip"] = false
                    keypointColors["R_Hip"] = false
                    tracker.wasWrong = true
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
            tracker.changeKeypoints(keypointColors)
        }
    }

    return tracker
}
