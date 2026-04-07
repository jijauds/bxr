package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker
import com.bxr.trainingapp.sessions.Reps

private val straightAngles = mapOf(
    //"L_Hand" to Pair(22.0,154.0), Occluded
    //"R_Hand" to Pair(4.0, 180.0),
    //"L_Elbow" to Pair(117.0, 179.0),
    "R_Elbow" to Pair(160.0, 180.0),
    "L_Knee" to Pair(135.0, 177.0),
    "R_Knee" to Pair(110.0, 174.0),
    //"L_Shoulder" to Pair(4.0, 172.0),
    "R_Shoulder" to Pair(73.0, 123.43),
    "L_Hip" to Pair(1.0, 116.0),
    "R_Hip" to Pair(60.0, 176.0)
)

private const val THRESHOLD = 25.0
private val checkError = GenericErrorChecker()

fun trackStraight(angleType: AngleType, tracker: FormTracker): FormTracker {
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
                    angles["R_Hand"]?.let { tracker.errorCounter.handX = it.x }
                    tracker.state = FormStates.inProgress
                }
            }
        }

        FormStates.inProgress -> {
            val checkStraight = checkStraight(angles, straightAngles,THRESHOLD)
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()

            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.errorCounter.reset()
                tracker.errorCounter.readyPunchNotFull = false
            } else {
                tracker.errorCounter.readyPunchNotFull = true
                tracker.currentErrors += checkStraight.errors.toMutableList()
            }

            // tracker.currentErrors.addAll(checkJab.errors)
            tracker.addKeyPoseErrors(checkStraight.errors)
            //tracker.changeKeypoints(checkStraight.keypoints)

            //Check if hands are wrong
            //Check lead hand placement -- OCCLUDED

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Straight")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                    keypointColors["R_Hand"] = false
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

            // Check if punch was stretched out
            if (tracker.errorCounter.readyPunchNotFull){
                if (angles["R_Elbow"]!!.angle !in 165.0..180.0) {
                    tracker.errorCounter.punchNotFull = true
                } else {
                    tracker.errorCounter.punchNotFull = false
                    tracker.errorCounter.readyPunchNotFull = false
                }
                if (angles["R_Hand"]!!.x < tracker.errorCounter.handX-0.01) {
                    tracker.errorCounter.punchNotFullCounter++
                    if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                        if (tracker.errorCounter.punchNotFull) {
                            tracker.addErrors(listOf("Punch not full"))
                            tracker.currentErrors.add("Punch not full")
                            tracker.errorCounter.punchNotFull = true
                            keypointColors["R_Elbow"] = false
                            tracker.wasWrong = true
                        }
                        tracker.errorCounter.punchNotFull = false
                    }
                } else {
                    tracker.errorCounter.punchNotFullCounter = 0
                }
            }
            if (angles["R_Hand"] != null){
                tracker.errorCounter.handX = angles["R_Hand"]!!.x
            }
            val atClimax = checkStraight.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
            tracker.changeKeypoints(keypointColors)
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()
            //tracker.currentErrors.addAll(checkGuard.errors)

            checkGuard.errors.forEach { error ->
                if (!tracker.currentErrors.contains(error)) {
                    tracker.currentErrors.add(error)
                }
            }

            if (angles["R_Hand"] != null){
                tracker.errorCounter.handX = angles["R_Hand"]!!.x
            }


            tracker.addKeyPoseErrors(checkGuard.errors)
            tracker.changeKeypoints(checkGuard.keypoints)

            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Straight")) {
                tracker.errorCounter.punchNotStraight++
                if (tracker.errorCounter.punchNotStraight > errorFrameCheck) {
                    tracker.errorCounter.punchNotStraight = 0
                    tracker.addErrors(listOf("Punch not straight"))
                    tracker.currentErrors.add("Punch not straight")
                    keypointColors["R_Hand"] = false
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
            }
            tracker.changeKeypoints(keypointColors)
        }
    }

    return tracker
}
