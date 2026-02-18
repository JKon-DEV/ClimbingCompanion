package com.example.bouldercompanion.ui

import TimerRoutine
import TimerRoutineDatabase
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.bouldercompanion.R
import com.example.bouldercompanion.viewmodel.TimerRoutineViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddEditTimerActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var setAmountInput: TextInputEditText
    private lateinit var repsPerSetInput: TextInputEditText
    private lateinit var hangTimeInput: TextInputEditText
    private lateinit var restBetweenRepsInput: TextInputEditText
    private lateinit var restBetweenSetsInput: TextInputEditText
    private lateinit var saveBtn: MaterialButton

    private var routineId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_timer)

        val dao = TimerRoutineDatabase.getDatabase(applicationContext).TimerRoutineDao()

        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TimerRoutineViewModel(dao) as T
            }
        })[TimerRoutineViewModel::class.java]

        nameInput = findViewById(R.id.inputTimerName)
        setAmountInput = findViewById(R.id.inputSetAmount)
        repsPerSetInput = findViewById(R.id.inputRepsPerSet)
        hangTimeInput = findViewById(R.id.inputHangTime)
        restBetweenRepsInput = findViewById(R.id.inputRestBetweenReps)
        restBetweenSetsInput = findViewById(R.id.inputRestBetweenSets)
        saveBtn = findViewById(R.id.btnSaveTimer)

        routineId = intent.getIntExtra("TIMER_ID", -1).takeIf { it != -1 }

        if (routineId != null) {
            viewModel.getRoutineByIdFlow(routineId!!)
                .asLiveData()
                .observe(this) { routine ->
                    routine?.let {
                        nameInput.setText(it.name)
                        setAmountInput.setText(it.sets.toString())
                        repsPerSetInput.setText(it.repsPerSet.toString())
                        hangTimeInput.setText(it.hangTime.toString())
                        restBetweenRepsInput.setText(it.restBetweenReps.toString())
                        restBetweenSetsInput.setText(it.restBetweenSets.toString())
                    }
                }
        }

        saveBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val setAmount = setAmountInput.text.toString().toIntOrNull()
            val repsPerSet = repsPerSetInput.text.toString().toIntOrNull()
            val hangTime = hangTimeInput.text.toString().toIntOrNull()
            val restBetweenReps = restBetweenRepsInput.text.toString().toIntOrNull()
            val restBetweenSets = restBetweenSetsInput.text.toString().toIntOrNull()

            if (
                name.isBlank() ||
                setAmount == null ||
                repsPerSet == null ||
                hangTime == null ||
                restBetweenReps == null ||
                restBetweenSets == null
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (routineId == null) {
                viewModel.addTimerRoutine(
                    TimerRoutine(
                        name = name,
                        sets = setAmount,
                        repsPerSet = repsPerSet,
                        hangTime = hangTime,
                        restBetweenReps = restBetweenReps,
                        restBetweenSets = restBetweenSets
                    )
                )
            } else {
                viewModel.updateTimerRoutine(
                    TimerRoutine(
                        id = routineId!!,
                        name = name,
                        sets = setAmount,
                        repsPerSet = repsPerSet,
                        hangTime = hangTime,
                        restBetweenReps = restBetweenReps,
                        restBetweenSets = restBetweenSets
                    )
                )
            }

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
