package com.example.bouldercompanion.timer

enum class TimerPhase {
    PREP, HANG, REST_REP, REST_SET, FINISHED
}

data class TimerState(
    var currentSet: Int = 1,
    var currentRep: Int = 1,
    var phase: TimerPhase = TimerPhase.HANG,
    var timeRemaining: Int = 0
)