package com.bxr.trainingapp.sessions
import com.bxr.trainingapp.forms.ErrorTypes

data class Reps(
    var total: Int,
    var correct: Int,
    var wrong: Int
)

class FormTracker {
    var state : FormStates = FormStates.notStarted
    var reps: Reps = Reps(0, 0, 0)
    var keyPoseErrors: MutableList<String> = mutableListOf()
    var errors: MutableList<String> = mutableListOf()
    var currentErrors: MutableList<String> = mutableListOf()
    var errorCounter = ErrorTypes()
    var wasWrong = false
    var keypoints: Map<String, Boolean> = mapOf()

    fun addReps(newReps: Reps) {
        this.reps.total += newReps.total
        this.reps.correct += newReps.correct
        this.reps.wrong += newReps.wrong
    }

    fun addKeyPoseErrors(newErrors: List<String>) {
        this.keyPoseErrors.addAll(newErrors) }

    fun addErrors(newErrors: List<String>) {
        this.errors.addAll(newErrors) }

    fun changeKeypoints(newKeypoints: Map<String, Boolean>){
        this.keypoints = newKeypoints
    }
}