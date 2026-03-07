package com.bxr.trainingapp.sessions
import com.bxr.trainingapp.forms.ErrorTypes


class FormTracker {
    var state : FormStates = FormStates.notStarted
    var reps: Triple<Int, Int, Int> = Triple(0, 0, 0)
    var keyPoseErrors: MutableList<String> = mutableListOf()
    var errors: MutableList<String> = mutableListOf()
    var currentErrors: MutableList<String> = mutableListOf()
    var errorCounter = ErrorTypes()

    var keypoints: Map<String, Boolean> = mapOf()

    fun addReps(newReps: Triple<Int, Int, Int>) {
        this.reps = Triple(this.reps.first + newReps.first, this.reps.second + newReps.second, this.reps.third + newReps.third)
    }

    fun addKeyPoseErrors(newErrors: List<String>) {
        this.keyPoseErrors.addAll(newErrors) }

    fun addErrors(newErrors: List<String>) {
        this.errors.addAll(newErrors) }

    fun changeKeypoints(newKeypoints: Map<String, Boolean>){
        this.keypoints = newKeypoints
    }
}