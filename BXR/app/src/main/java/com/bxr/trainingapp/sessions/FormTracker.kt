package com.bxr.trainingapp.sessions


class FormTracker {
    var state : FormStates = FormStates.notStarted
    var reps: Triple<Int, Int, Int> = Triple(0, 0, 0)

    fun addReps(reps: Triple<Int, Int, Int>) {
        this.reps = Triple(this.reps.first + reps.first, this.reps.second + reps.second, this.reps.third + reps.third)
    }
}