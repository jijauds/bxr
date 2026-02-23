package com.bxr.trainingapp.sessions


class FormTracker {
    var state : FormStates = FormStates.notStarted
    var reps: Triple<Int, Int, Int> = Triple(0, 0, 0)

    fun addReps(newReps: Triple<Int, Int, Int>) {
        this.reps = Triple(this.reps.first + newReps.first, this.reps.second + newReps.second, this.reps.third + newReps.third)
    }
}