package com.bxr.trainingapp.sessions

import com.bxr.trainingapp.forms.FormTypes
import java.time.Instant

data class SessionTracker (
    var startTime : Instant,
    var endTime : Instant,
    var formState : FormTracker,
    var handedness : Handedness
)

