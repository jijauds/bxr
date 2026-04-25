package com.bxr.trainingapp.sessions
import android.util.Log
import com.bxr.trainingapp.forms.ErrorTypes
import com.bxr.trainingapp.model.FormError

data class RepResult(
    val repNumber: Int,
    val errors: MutableList<FormError> = mutableListOf(),
    var isCorrect: Boolean = true
)

data class Reps(
    var total: Int,
    var correct: Int,
    var wrong: Int
)

class FormTracker {

    var state : FormStates = FormStates.notStarted
    var reps: Reps = Reps(0, 0, 0)

    var repResults: MutableList<RepResult> = mutableListOf()
    private var currentRep: RepResult? = null
    var keyPoseErrors: MutableList<FormError> = mutableListOf()
    var errors: MutableList<FormError> = mutableListOf()
    var currentErrors: MutableList<FormError> = mutableListOf()
    var errorCounter = ErrorTypes()
    var wasWrong = false
    var keypoints: Map<String, Boolean> = mapOf()
    fun addKeyPoseErrors(newErrors: List<FormError>) {
        this.keyPoseErrors.addAll(newErrors) }

    fun addErrors(newErrors: List<FormError>) {
        if (newErrors.isEmpty()) return
        wasWrong = true

        for (error in newErrors) {
            if (!errors.any { it == error }) {
                errors.add(error)
            }
        }
    }

    fun changeKeypoints(newKeypoints: Map<String, Boolean>){
        this.keypoints = newKeypoints
    }

    fun startRep() {
        wasWrong = false
        val repNumber = reps.total + 1
        currentRep = RepResult(repNumber)
    }

    fun endRep() {
        currentRep?.let {
            it.isCorrect = !wasWrong
            it.errors.addAll(errors)

            repResults.add(it)
            reps.total++
            if (!wasWrong) reps.correct++
            else reps.wrong++
        }
        Log.d("ERRORS FOR THE REP", errors.toString())
        currentRep = null
        errors.clear()
        wasWrong = false
    }
}

