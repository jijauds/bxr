package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.FormStates
import com.bxr.trainingapp.sessions.FormTracker

private val rearHookAngles = mapOf(
    //"L_Hand" to Pair(6.0,120.0),
    //"R_Hand" to Pair(119.0,180.0),
    //"L_Elbow" to Pair(8.0,180.0),
    //"R_Elbow" to Pair(0.0,180.0),
    "L_Knee" to Pair(132.0,180.0),
    "R_Knee" to Pair(117.0,178.0),
    //"L_Shoulder" to Pair(19.0,139.0),
    "R_Shoulder" to Pair(96.0,122.0),
    "L_Hip" to Pair(5.0,160.0),
    "R_Hip" to Pair(10.0,150.0)
)

private const val THRESHOLD = 20.0
private val checkError = GenericErrorChecker()

fun trackRearHook(angleType: AngleType, tracker: FormTracker): FormTracker {
    val angles = angleType.angles

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
                    tracker.errorCounter.handX = angles["R_Elbow"]!!.y
                    tracker.state = FormStates.inProgress
                }
            }
        }

        FormStates.inProgress -> {
            val checkRearHook = checkRearHookAngle(angles, rearHookAngles, THRESHOLD)
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()


            val atGuard = checkGuard.errors.isEmpty()
            if (atGuard) {
                tracker.errorCounter.reset()
                tracker.errorCounter.readyPunchNotFull = false
            } else {
                tracker.errorCounter.readyPunchNotFull = true
            }

            // tracker.currentErrors.addAll(checkRearHook.errors)
            tracker.addKeyPoseErrors(checkRearHook.errors)
            //tracker.changeKeypoints(checkRearHook.keypoints)

            //Check if hands are wrong
            //Check rear hand placement
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Rear Hook")) {
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
            //Check if leaning backward
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
            // Check if Elbow too high
            if (angles["R_Elbow"]?.y != null && angles["R_Shoulder"]?.y != null) {
                if (angles["R_Elbow"]!!.y < angles["R_Shoulder"]!!.y - 0.05){
                    keypointColors["R_Elbow"] = false
                    tracker.addErrors(listOf("Elbow too high"))
                    tracker.currentErrors.add("Elbow too high")
                }
            }

            // Check if punch was stretched out
            if (tracker.errorCounter.readyPunchNotFull){
                if (angles["R_Hand"]?.x != null && angles["R_Elbow"]?.x != null && angles["R_Elbow"]?.x != null) {
                    if (angles["R_Hand"]!!.x in angles["R_Elbow"]!!.x-0.01..angles["R_Elbow"]!!.x+0.01 && angles["R_Hand"]!!.y in angles["R_Elbow"]!!.y-0.01..angles["R_Elbow"]!!.y+0.01 ) {
                        tracker.errorCounter.punchNotFull = false
                        tracker.errorCounter.readyPunchNotFull = false
                    } else {
                        tracker.errorCounter.punchNotFull = true
                    }
                }
                if (angles["R_Elbow"]!!.y > tracker.errorCounter.handX+0.01) {
                    tracker.errorCounter.punchNotFullCounter++
                    if (tracker.errorCounter.punchNotFullCounter > errorFrameCheck) {
                        if (tracker.errorCounter.punchNotFull) {
                            tracker.addErrors(listOf("Punch not full"))
                            tracker.currentErrors.add("Punch not full")
                            tracker.errorCounter.punchNotFull = true
                            keypointColors["R_Elbow"] = false
                            tracker.wasWrong = true
                        }
                        tracker.errorCounter.punchNotFull = true
                    }
                } else {
                    tracker.errorCounter.punchNotFullCounter = 0
                }
            }
            if (angles["R_Elbow"] != null){
                tracker.errorCounter.handX = angles["R_Elbow"]!!.y
            }
            val atClimax = checkRearHook.errors.isEmpty()
            if (atClimax) {
                tracker.state = FormStates.completed
            }
            tracker.changeKeypoints(keypointColors)
        }

        FormStates.completed -> {
            val checkGuard = checkAngle(angles, stanceAngles, THRESHOLD)
            val keypointColors = checkGuard.keypoints.toMutableMap()

            checkGuard.errors.forEach { error ->
                if (!tracker.currentErrors.contains(error)) {
                    tracker.currentErrors.add(error)
                }
            }

            tracker.addKeyPoseErrors(checkGuard.errors)

            //Check if hands are wrong
            //Check punch if straight
            if (checkError.punchStraightCheck(angles, "Rear Hook")) {
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
            //Check if leaning backward

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
