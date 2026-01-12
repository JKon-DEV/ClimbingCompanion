package com.example.bouldercompanion.ui

import TimerRoutine
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bouldercompanion.R

class TimerRoutineAdapter(private var timers: List<TimerRoutine>, private val onClick: (TimerRoutine) -> Unit, private val onLongClick: (TimerRoutine) -> Unit) :

    RecyclerView.Adapter<TimerRoutineAdapter.TimerRoutineViewHolder>() {

    // What will be displayed in the recyclerView
    class TimerRoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
    }

    // base constructor oncreation of a viewholder, using out layout item_climb
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerRoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timer, parent, false)
        return TimerRoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerRoutineViewHolder, position: Int) {
        val timerRoutine = timers[position]
        holder.name.text = timerRoutine.name

        holder.itemView.setOnClickListener { onClick(timerRoutine) }
        holder.itemView.setOnLongClickListener {
            onLongClick(timerRoutine)
            true
        }
    }

    override fun getItemCount() = timers.size

    fun updateData(newTimers: List<TimerRoutine>) {
        timers = newTimers
        notifyDataSetChanged()
    }
}