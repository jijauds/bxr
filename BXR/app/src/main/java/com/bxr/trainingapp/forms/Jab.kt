package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker

private val jabAngles = mapOf(
    "L_Hand" to Pair(152.0, 180.0),
    //"R_Hand" to Pair(0.0, 55.0),
    "L_Elbow" to Pair(146.0, 180.0),
    "R_Elbow" to Pair(26.0, 71.0),
    "L_Knee" to Pair(150.0, 180.0),
    "R_Knee" to Pair(120.0,180.0),
    "L_Shoulder" to Pair(67.0, 111.0),
    "R_Shoulder" to Pair(0.0, 87.0),
    "L_Hip" to Pair(78.0,121.0),
    "R_Hip" to Pair(80.0,115.0)
)


private const val THRESHOLD = 20.0
private val checkError = GenericErrorChecker()

fun trackJab(angleType: AngleType, tracker: FormTracker): FormTracker {
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
                    angles["L_Hand"]?.let { tracker.errorCounter.handX = it.x }
                    tracker.state = FormStates.inProgress
                }
            }
        }

        FormStates.inProgress -> {
            val checkJab = checkAngle(angles, jabAngles, THRESHOLD)
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()

            // tracker.currentErrors.addAll(checkJab.errors)
            tracker.addKeyPoseErrors(checkJab.errors)
            // tracker.changeKeypoints(checkJab.keypoints)

            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.guardHandCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.addErrors(listOf("Guard hand goes down"))
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.currentErrors.add("Guard hand goes down")
                    keypointColors["R_Hand"] = false
                }
            } else {
                tracker.errorCounter.guardHandGoesDown = 0
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Jab")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                    keypointColors["L_Hand"] = false
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
                }
            } else {
                tracker.errorCounter.leaningBackwards = 0
            }

            // Check if punch was stretched out
            if (angles["L_Elbow"]!!.angle !in 165.0..180.0) {
                tracker.errorCounter.punchNotFull = true
            } else tracker.errorCounter.punchNotFull = false


            if (angles["L_Hand"]!!.x < tracker.errorCounter.handX-0.01) {
                tracker.errorCounter.punchNotFullCounter++
                if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                    if (tracker.errorCounter.punchNotFull) {
                        tracker.addErrors(listOf("Punch not full"))
                        tracker.currentErrors.add("Punch not full")
                        tracker.errorCounter.punchNotFull = true
                        keypointColors["L_Elbow"] = false
                    }
                    tracker.errorCounter.punchNotFull = false
                }
            } else {
                tracker.errorCounter.punchNotFullCounter = 0
            }

            if (angles["L_Hand"] != null){
                tracker.errorCounter.handX = angles["L_Hand"]!!.x
            }

            val atClimax = checkJab.errors.isEmpty()

            // Transition to completed based on movement climax

            if (atClimax) {
                tracker.state = FormStates.completed
            }
            tracker.changeKeypoints(keypointColors)
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()
            // Fixed potential duplicate accumulation in currentErrors
            checkGuard.errors.forEach { error ->
                if (!tracker.currentErrors.contains(error)) {
                    tracker.currentErrors.add(error)
                }
            }

            if (angles["L_Hand"] != null){
                tracker.errorCounter.handX = angles["L_Hand"]!!.x
            }
            tracker.addKeyPoseErrors(checkGuard.errors)
            // tracker.changeKeypoints(checkGuard.keypoints)

            //Check if hands are wrong
            //Check rear hand placement
            if (checkError.guardHandCheck(angles)) {
                tracker.errorCounter.guardHandGoesDown++
                if (tracker.errorCounter.guardHandGoesDown > errorFrameCheck) {
                    tracker.errorCounter.guardHandGoesDown = 0
                    tracker.addErrors(listOf("Guard hand goes down"))
                    tracker.currentErrors.add("Guard hand goes down")
                    keypointColors["R_Hand"] = false
                }
            } else {
                tracker.errorCounter.guardHandGoesDown = 0
            }
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Jab")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                    keypointColors["L_Hand"] = false
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