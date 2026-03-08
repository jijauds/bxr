package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.Coords
import com.bxr.trainingapp.sessions.FormTracker
import kotlin.collections.set

class GenericErrorChecker {
    private val threshhold = 0.1;
    private val errorCheckValues = mapOf(
        //"guardHandGoesDown" to 180.0,
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
        val hand = angles["R_Hand"] ?: return false
        val shoulder = angles["R_Shoulder"] ?: return false

        return hand.y > (shoulder.y + threshhold)
    }

    fun punchStraightCheck(angles: Map<String, Coords>, punchType: String): Boolean {
        when (punchType) {
            "Jab" -> {
                val hand = angles["L_Hand"] ?: return false
                val shoulder = angles["L_Shoulder"] ?: return false

                return hand.y !in (shoulder.y - 0.05)..(shoulder.y + 0.05)
            }
            "Straight" -> {
                val hand = angles["R_Hand"] ?: return false
                val shoulder = angles["R_Shoulder"] ?: return false

                return hand.y !in (shoulder.y - 0.05)..(shoulder.y + 0.05)
            }
            "Lead Hook" -> {
                val hand = angles["L_Hand"] ?: return false
                val shoulder = angles["L_Shoulder"] ?: return false

                return hand.y !in (shoulder.y - 0.05)..(shoulder.y + 0.05)
            }
        }
        return false
    }

    fun leanForwardCheck(angles: Map<String, Coords>): Boolean {
        val leftshoulder = angles["L_Shoulder"] ?: return false
        val rightshoulder = angles["L_Shoulder"] ?: return false

        return leftshoulder.y < (rightshoulder.y + threshhold)
    }

    fun leanBackCheck(angles: Map<String, Coords>): Boolean {
        val leftshoulder = angles["L_Shoulder"] ?: return false
        val rightshoulder = angles["L_Hip"] ?: return false

        return leftshoulder.y > (rightshoulder.y + threshhold)
    }
}