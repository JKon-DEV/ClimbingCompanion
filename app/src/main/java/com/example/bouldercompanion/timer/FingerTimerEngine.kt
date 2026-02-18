package com.example.bouldercompanion.timer

import TimerRoutine
import android.os.CountDownTimer

class FingerTimerEngine(
    private val routine: TimerRoutine,
    private val onTick: (TimerState) -> Unit,
    private val onFinish: () -> Unit
) {

    private val state = TimerState(
        phase = TimerPhase.PREP,
        timeRemaining = 10   // Global 10s countdown before any routine
    )

    private var timer: CountDownTimer? = null

    fun start() {
        runPhase()
    }

    fun stop() {
        timer?.cancel()
    }

    private fun runPhase() {
        timer?.cancel()

        timer = object : CountDownTimer(
            state.timeRemaining * 1000L,
            1000L
        ) {
            override fun onTick(ms: Long) {
                state.timeRemaining = (ms / 1000).toInt()
                onTick(state)
            }

            override fun onFinish() {
                advancePhase()
            }
        }.start()
    }

    private fun advancePhase() {
        when (state.phase) {

            TimerPhase.PREP -> {
                state.phase = TimerPhase.HANG
                state.timeRemaining = routine.hangTime
            }

            TimerPhase.HANG -> {
                if (routine.repsPerSet > 1 && routine.restBetweenReps > 0) {
                    state.phase = TimerPhase.REST_REP
                    state.timeRemaining = routine.restBetweenReps
                } else {
                    nextRepOrSet()
                    return
                }
            }

            TimerPhase.REST_REP -> {
                nextRepOrSet()
                return
            }

            TimerPhase.REST_SET -> {
                state.currentSet++
                state.currentRep = 1
                state.phase = TimerPhase.HANG
                state.timeRemaining = routine.hangTime
            }

            TimerPhase.FINISHED -> return
        }

        runPhase()
    }

    private fun nextRepOrSet() {
        if (state.currentRep < routine.repsPerSet) {
            state.currentRep++
            state.phase = TimerPhase.HANG
            state.timeRemaining = routine.hangTime
        } else {
            if (state.currentSet < routine.sets) {
                state.phase = TimerPhase.REST_SET
                state.timeRemaining = routine.restBetweenSets
            } else {
                state.phase = TimerPhase.FINISHED
                onFinish()
                return
            }
        }

        runPhase()
    }
}
