package com.example.bouldercompanion.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bouldercompanion.R
import ClimbDatabase
import TimerRoutine
import android.app.AlertDialog
import com.example.bouldercompanion.viewmodel.ClimbViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import java.io.File
import android.widget.Button
import com.example.bouldercompanion.viewmodel.TimerRoutineViewModel
import com.example.bouldercompanion.timer.FingerTimerEngine



private var timerEngine: FingerTimerEngine? = null

class RunTimerRoutine : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_timer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val dao = TimerRoutineDatabase.getDatabase(applicationContext).TimerRoutineDao()

        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TimerRoutineViewModel(dao) as T
            }
        }).get(TimerRoutineViewModel::class.java)


        // get the climbId and load in to view
        val timerId = intent.getIntExtra("TIMER_ID", -1)

        lifecycleScope.launch {
            viewModel.getRoutineByIdFlow(timerId).collect { routine ->
                routine?.let {
                    setupTimerUI(it)
                }
            }
        }
    }
    private fun setupTimerUI(routine: TimerRoutine) {

        val nameText = findViewById<TextView>(R.id.timerName)
        val phaseText = findViewById<TextView>(R.id.phaseText)
        val timeText = findViewById<TextView>(R.id.timeText)
        val setRepText = findViewById<TextView>(R.id.setRepText)

        val startBtn = findViewById<Button>(R.id.startButton)
        val stopBtn = findViewById<Button>(R.id.stopButton)

        nameText.text = routine.name
        setRepText.text = "Set 1/${routine.sets} | " + "Rep 1/${routine.repsPerSet}"
        phaseText.text = "PREP"


        timerEngine = FingerTimerEngine(
            routine = routine,
            onTick = { state ->
                runOnUiThread {
                    phaseText.text = state.phase.name
                    timeText.text = "${state.timeRemaining}s"
                    setRepText.text =
                        "Set ${state.currentSet}/${routine.sets} | " + "Rep ${state.currentRep}/${routine.repsPerSet}"
                }
            },
            onFinish = {
                runOnUiThread {
                    phaseText.text = "DONE"
                    timeText.text = "💪"
                }
            }
        )

        startBtn.setOnClickListener {
            timerEngine?.start()
        }

        stopBtn.setOnClickListener {
            timerEngine?.stop()
        }
    }

}


