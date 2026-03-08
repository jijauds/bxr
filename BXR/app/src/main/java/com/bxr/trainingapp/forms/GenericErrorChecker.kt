package com.bxr.trainingapp.forms

import android.util.Log
import com.bxr.trainingapp.data.Coords
import com.bxr.trainingapp.sessions.FormTracker
import kotlin.collections.set

class GenericErrorChecker {
    private val threshhold = 0.1;

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

        return hand.y > (shoulder.y + threshhold)
    }

    fun leadUpperCutGuardCheck(angles: Map<String, Coords>): Boolean {
        val hand = angles["R_Hand"] ?: return false
        val shoulder = angles["L_Shoulder"] ?: return false

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
            "Lead Upper Cut" -> {
                val hand = angles["L_Hand"] ?: return false
                val shoulder = angles["L_Shoulder"] ?: return false

                return hand.y < shoulder.y - 0.03
            }
            "Rear Upper Cut" -> {
                val hand = angles["R_Hand"] ?: return false
                val shoulder = angles["R_Shoulder"] ?: return false

                return hand.y < shoulder.y - 0.03
            }
        }
        return false
    }

    fun leanForwardCheck(angles: Map<String, Coords>, threshold: Double = 0.07): Boolean {
        val diff = torsoCenterX(angles) ?: return false
        Log.d("DIFF", diff.toString())
        return diff > threshold
    }

    fun leanBackCheck(angles: Map<String, Coords>, threshold: Double = 0.07): Boolean {
        val diff = torsoCenterX(angles) ?: return false
        return diff < -threshold
    }
}