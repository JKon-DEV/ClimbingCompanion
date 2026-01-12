package com.example.bouldercompanion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import TimerRoutineDao
import TimerRoutine
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.launch


class TimerRoutineViewModel(private val dao: TimerRoutineDao) : ViewModel() {
    val timers = dao.getAllFlow().asLiveData()

    fun addTimerRoutine(timer: TimerRoutine) = viewModelScope.launch {
        dao.insert(timer)
    }

    fun updateTimerRoutine(timer: TimerRoutine) = viewModelScope.launch {
        dao.update(timer)
    }

    fun deleteTimerRoutine(timer: TimerRoutine) = viewModelScope.launch {
        dao.delete(timer)
    }
}