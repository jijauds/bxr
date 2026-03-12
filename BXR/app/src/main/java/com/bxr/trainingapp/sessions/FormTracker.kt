package com.bxr.trainingapp.sessions
import com.bxr.trainingapp.forms.ErrorTypes

data class RepResult(
    val repNumber: Int,
    val errors: MutableList<String> = mutableListOf(),
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
    var keyPoseErrors: MutableList<String> = mutableListOf()
    var errors: MutableList<String> = mutableListOf()
    var currentErrors: MutableList<String> = mutableListOf()
    var errorCounter = ErrorTypes()
    var wasWrong = false
    var keypoints: Map<String, Boolean> = mapOf()
    var errorsWithDuplicates: MutableList<String> = mutableListOf()

    fun addReps(newReps: Reps) {
        this.reps.total += newReps.total
        this.reps.correct += newReps.correct
        this.reps.wrong += newReps.wrong
    }

    fun addKeyPoseErrors(newErrors: List<String>) {
        this.keyPoseErrors.addAll(newErrors) }

    fun addErrors(newErrors: List<String>) {
        if (newErrors.isEmpty()) return
        wasWrong = true

        for (error in newErrors) {
            if (!errors.contains(error)) {
                errors.add(error)
            }
        }
    }

    fun changeKeypoints(newKeypoints: Map<String, Boolean>){
        this.keypoints = newKeypoints
    }

    fun startRep() {
        val repNumber = reps.total + 1
        currentRep = RepResult(repNumber)
    }

    fun endRep() {
        currentRep?.let {
            it.isCorrect = !wasWrong

            it.errors.clear()
            it.errors.addAll(errors.distinct())

            repResults.add(it)
            reps.total++
            if (!wasWrong) reps.correct++
            else reps.wrong++
        }
        currentRep = null
        errors.clear()
        currentErrors.clear()
    }
}

