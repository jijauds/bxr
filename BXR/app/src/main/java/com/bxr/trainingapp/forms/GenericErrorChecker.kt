package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.Coords
import com.bxr.trainingapp.sessions.FormTracker

class GenericErrorChecker {
    private val threshold = 20.0;
    private val errorCheckValues = mapOf(
        "guardHandGoesDown" to 180.0,
        "punchNotStraight" to 180.0,
        //"leaningForward" to 0,
        //"leaningBackwards" to 0,
        //"punchNotFull" to 0,
        //"elbowsFlaring" to 0,
        //"wrongFootForward" to 0,
        //"feetNotPivoting" to 0,
        //"startingPosition" to 0
    )
    fun guardHandCheck(angles: Map<String, Coords>): Boolean {
        if (angles["R_Hand"]!!.angle !in errorCheckValues["guardHandGoesDown"]!! - threshold..errorCheckValues["guardHandGoesDown"]!! + threshold) {
            return true
        } else {
            return false
        }
    }

    fun punchStraightCheck(angles: Map<String, Coords>): Boolean {
        if (angles["L_Hand"]!!.angle !in errorCheckValues["punchNotStraight"]!! - threshold..errorCheckValues["punchNotStraight"]!! + threshold) {
            return true
        } else {
            return false
        }
    }

    fun leanBackCheck(angles: Map<String, Coords>): Boolean {
        if (angles["L_Shoulder"]!!.y > angles["R_Shoulder"]!!.y+threshold) {
            return true
        } else {
            return false
        }
    }

    fun leanForwardCheck(angles: Map<String, Coords>): Boolean {
        if (angles["L_Shoulder"]!!.y < angles["R_Shoulder"]!!.y-threshold) {
            return true
        } else {
            return false
        }
    }
}