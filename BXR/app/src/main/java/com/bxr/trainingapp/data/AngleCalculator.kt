package com.bxr.trainingapp.data

import android.graphics.PointF
import kotlin.math.acos
import kotlin.math.sqrt

fun calculateAngle(pointA: Pair<Float, Float>, pointB: Pair<Float, Float>, pointC: Pair<Float, Float>): Double {
    // Formula using dot product (B as central point):
    // cos(theta) = (AB dot BC) / (||AB|| x ||BC||)

    // Lengths of each vector (AB, BC)
    val ABx = pointB.first - pointA.first
    val ABy = pointB.second - pointA.second

    val BCx = pointC.first - pointB.first
    val BCy = pointC.second - pointB.second

    // Dot product and vector magnitudes
    val dotProduct = ABx * BCx + ABy * BCy
    val magnitudeAB: Float = sqrt(ABx * ABx + ABy * ABy)
    val magnitudeBC: Float = sqrt(BCx * BCx + BCy * BCy)

    // Check for division by 0
    if (magnitudeAB == 0.0f || magnitudeBC == 0.0f) return 0.0

    /* Calculate for angle:
        - Obtain cos(theta) using dot product and magnitudes
        - Ensure cos(theta) is within bounds [-1.0, 1.0]
        - Compute for arccos(cos(theta))
    */
    val angle = acos((dotProduct / (magnitudeAB * magnitudeBC)).coerceIn(-1.0f, 1.0f))

    // Return value in degrees
    // Not finalized yet, (Math.PI - angle) is just to force straight lines to show 180 degrees---but possibly needs a bit more computation to distinguish direction of angle
    return (Math.PI - angle) * (180 / Math.PI)
}