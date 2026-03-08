package com.bxr.trainingapp.forms

data class ErrorTypes (
    var guardHandGoesDown: Int = 0,
    var punchNotStraight: Int = 0,
    var leaningForward: Int = 0,
    var leaningBackwards: Int = 0,
    var punchNotFull: Boolean = true,
    var punchNotFullCounter: Int = 0,
    var elbowsFlaring: Int = 0,
    var wrongFootForward: Int = 0,
    var feetNotPivoting: Int = 0,
    var startingPosition: Int = 0,
    var handX: Float = 0f,
    var didNotPivotHip: Int = 0
) {
    fun reset() {
        guardHandGoesDown = 0
        punchNotStraight = 0
        leaningForward = 0
        leaningBackwards = 0
        punchNotFull = true
        punchNotFullCounter = 0
        elbowsFlaring = 0
        wrongFootForward = 0
        feetNotPivoting = 0
        startingPosition = 0
        handX = 0f
        didNotPivotHip = 0
    }
}