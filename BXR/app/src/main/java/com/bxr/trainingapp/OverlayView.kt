/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bxr.trainingapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.se.omapi.Session
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.max
import kotlin.math.min
import com.bxr.trainingapp.data.calculateAngle
import android.util.Log
import com.bxr.trainingapp.data.Angles
import com.bxr.trainingapp.data.AngleType
import com.bxr.trainingapp.sessions.SessionTracker


class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    var rotationDegrees = 0
    var isFrontCamera = false
    private var results: PoseLandmarkerResult? = null
    private var keypointErrors : Map<String, Boolean> = mapOf()
    private val pointPaint = Paint()
    private val linePaintCorrect = Paint()
    private val linePaintWrong = Paint()

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    private var offsetX = 0f
    private var offsetY = 0f
    private val selected = setOf(0, 11, 12, 13, 14, 15, 16, 23, 24, 25, 26, 27, 28, 29, 30)
    private val selected_lines = listOf(
        Pair(12,11),
        Pair(12,14),
        Pair(14,16),
        Pair(11,13),
        Pair(13,15),
        Pair(12,24),
        Pair(11,23),
        Pair(24,23),
        Pair(24,26),
        Pair(23,25),
        Pair(26,30),
        Pair(25,29)
    )

    private val keypoint_name_pairing = mapOf<Int, String>(
        15 to "L_Hand" ,
        //"R_Hand" to Triple(12,16,11),
        13 to "L_Elbow" ,
        14 to "R_Elbow" ,
        25 to "L_Knee" ,
        26 to "R_Knee" ,
        11 to "L_Shoulder" ,
        12 to "R_Shoulder" ,
        23 to "L_Hip" ,
        24 to "R_Hip" ,
    )

    init {
        initPaints()
    }

    private fun initPaints() {
        linePaintCorrect.color =
            ContextCompat.getColor(context!!, R.color.mp_color_primary)
        linePaintCorrect.strokeWidth = 12f
        linePaintCorrect.style = Paint.Style.STROKE

        linePaintWrong.color =
            ContextCompat.getColor(context!!, R.color.mp_color_wrong)
        linePaintWrong.strokeWidth = 12f
        linePaintWrong.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = 12f
        pointPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // canvas.rotate(rotationDegrees.toFloat(), width / 2f, height / 2f)

        canvas.scale(-1f, 1f, width / 2f, height / 2f)


        val result = results ?: return
        if (result.landmarks().isEmpty()) return

        val landmarks = result.landmarks()[0]

        selected.forEach { index ->
            if (index >= landmarks.size) return@forEach

            val landmark = landmarks[index]
            val x = landmark.x() * imageWidth * scaleFactor + offsetX
            val y = landmark.y() * imageHeight * scaleFactor + offsetY
            canvas.drawPoint(x, y, pointPaint)
        }

        selected_lines.forEach { (startIdx, endIdx) ->
            if (startIdx >= landmarks.size || endIdx >= landmarks.size) return@forEach

            val start = landmarks[startIdx]
            val end = landmarks[endIdx]

            val startX = start.x() * imageWidth * scaleFactor + offsetX
            val startY = start.y() * imageHeight * scaleFactor + offsetY
            val endX = end.x() * imageWidth * scaleFactor + offsetX
            val endY = end.y() * imageHeight * scaleFactor + offsetY

            if (keypointErrors[keypoint_name_pairing[startIdx]] == true || keypointErrors[keypoint_name_pairing[endIdx]] == true){
                canvas.drawLine(startX, startY, endX, endY, linePaintCorrect)
            }
            else {
                canvas.drawLine(startX, startY, endX, endY, linePaintWrong)
            }
        }
    }

    fun setResults(
        result: PoseLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode,
        keypoint_error: Map<String, Boolean>
    ) {

        if (imageWidth == 0 || imageHeight == 0) return
        keypointErrors = keypoint_error

        results = result
        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight

        scaleFactor = max(scaleX, scaleY)

        val scaledWidth = imageWidth * scaleFactor
        val scaledHeight = imageHeight * scaleFactor

        offsetX = (width - scaledWidth) / 2f
        offsetY = (height - scaledHeight) / 2f

        invalidate()
    }
}
