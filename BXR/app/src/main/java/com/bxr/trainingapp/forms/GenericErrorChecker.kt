package com.bxr.trainingapp.forms

import com.bxr.trainingapp.data.Coords
import com.bxr.trainingapp.sessions.FormTracker
import kotlin.collections.set

class GenericErrorChecker {
    private val threshold = 0.1;
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

    fun torsoCenterX(angles: Map<String, Coords>): Double? {

        val lShoulder = angles["L_Shoulder"] ?: return null
        val rShoulder = angles["R_Shoulder"] ?: return null
        val lHip = angles["L_Hip"] ?: return null
        val rHip = angles["R_Hip"] ?: return null

        val shoulderCenter = (lShoulder.x + rShoulder.x) / 2
        val hipCenter = (lHip.x + rHip.x) / 2

        return (shoulderCenter - hipCenter).toDouble()
    }
    fun guardHandCheck(angles: Map<String, Coords>): Boolean {
        val hand = angles["R_Hand"] ?: return false
        val shoulder = angles["R_Shoulder"] ?: return false

        return hand.y > (shoulder.y + threshold)
    }

    fun punchStraightCheck(angles: Map<String, Coords>): Boolean {
        val hand = angles["L_Hand"] ?: return false
        val shoulder = angles["L_Shoulder"] ?: return false

        return hand.y !in (shoulder.y - 0.05)..(shoulder.y + 0.05)
    }

    fun leanForwardCheck(angles: Map<String, Coords>, threshold: Double = 0.03): Boolean {
        val diff = torsoCenterX(angles) ?: return false
        return diff < -threshold
    }

    fun leanBackCheck(angles: Map<String, Coords>, threshold: Double = 0.03): Boolean {
        val diff = torsoCenterX(angles) ?: return false
        return diff > threshold
    }
}