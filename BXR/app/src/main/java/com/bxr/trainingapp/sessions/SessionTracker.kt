package com.bxr.trainingapp.sessions

import java.time.Instant

data class SessionTracker (
    var startTime : Instant,
    var endTime : Instant,
    var formState : FormTracker,
    var handedness : Handedness,
)

