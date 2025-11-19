package com.example.bouldercompanion.ui

import Climb
import coil.load
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bouldercompanion.R
import java.io.File

class ClimbAdapter(private var climbs: List<Climb>, private val onClick: (Climb) -> Unit, private val onLongClick: (Climb) -> Unit) :

    RecyclerView.Adapter<ClimbAdapter.ClimbViewHolder>() {

        // What will be displayed in the recyclerView
    class ClimbViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val grade: TextView = view.findViewById(R.id.tvGrade)
        val status: TextView = view.findViewById(R.id.tvStatus)

        val backgroundImage: ImageView = view.findViewById(R.id.bgImage)
    }


    // base constructor oncreation of a viewholder, using out layout item_climb
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClimbViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_climb, parent, false)
        return ClimbViewHolder(view)
    }

    // Update the a viewHolder with our climb specific data
    override fun onBindViewHolder(holder: ClimbViewHolder, position: Int) {
        val climb = climbs[position]
        holder.name.text = climb.name
        holder.grade.text = climb.grade
        holder.status.text = climb.status

        // using coil to load images, was getting stuttering this seems like a good solution
        val imagePath = climb.imageUri
        if (!imagePath.isNullOrBlank()) {
            val file = File(imagePath)
            if (file.exists()) {
                holder.backgroundImage.visibility = View.VISIBLE
                holder.backgroundImage.alpha = 0.8f
                holder.backgroundImage.load(file) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.placeholder_image)
                    size(400, 400)
                }
            } else {
                holder.backgroundImage.visibility = View.GONE
            }
        } else {
            holder.backgroundImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onClick(climb) }
        holder.itemView.setOnLongClickListener {
            onLongClick(climb)
            true
        }
    }

    override fun getItemCount() = climbs.size

    fun updateData(newClimbs: List<Climb>) {
        climbs = newClimbs
        notifyDataSetChanged()
    }
}