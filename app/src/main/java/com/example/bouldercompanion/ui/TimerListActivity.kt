package com.example.bouldercompanion.ui

import TimerRoutine
import TimerRoutineDao
import TimerRoutineDatabase
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bouldercompanion.R
import com.example.bouldercompanion.viewmodel.TimerRoutineViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton




private lateinit var addTimerButton: FloatingActionButton
private lateinit var dao: TimerRoutineDao
private lateinit var timerList: RecyclerView
private lateinit var adapter: TimerRoutineAdapter

class TimerListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addTimerButton = findViewById<FloatingActionButton>(R.id.addTimerButton)
        timerList = findViewById<RecyclerView>(R.id.timerList)
        timerList.layoutManager = LinearLayoutManager(this)

        dao = TimerRoutineDatabase.getDatabase(this).TimerRoutineDao()

        val timerRoutineViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Casting as T is generic type
                return TimerRoutineViewModel(dao) as T
            }
        }).get(TimerRoutineViewModel::class.java)

        adapter = TimerRoutineAdapter(
            emptyList(),
            onClick = { timer ->
                val intent = Intent(this, RunTimerRoutine::class.java)
                intent.putExtra("TIMER_ID", timer.id)
                startActivity(intent)
            },
            onLongClick = { timer ->
            }
        )

        // Connect recyclerview with adapter
        timerList.adapter = adapter

        // Update recyclerview with climbs
        timerRoutineViewModel.timers.observe(this) { timers ->
            adapter.updateData(timers)
        }

        addTimerButton.setOnClickListener {
        }
    }
}