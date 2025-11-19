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
import android.app.AlertDialog
import com.example.bouldercompanion.viewmodel.ClimbViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import java.io.File
import android.widget.Button

class ViewClimb : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_climb)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = findViewById<TextView>(R.id.climbName)
        val grade = findViewById<TextView>(R.id.climbGrade)
        val location = findViewById<TextView>(R.id.climbLocation)
        val status = findViewById<TextView>(R.id.climbStatus)
        val notes = findViewById<TextView>(R.id.climbNotes)
        val image = findViewById<ImageView>(R.id.image)
        val viewVideoBtn = findViewById<MaterialButton>(R.id.btnViewVideo)
        val deleteBtn = findViewById<Button>(R.id.deleteButton)

        val dao = ClimbDatabase.getDatabase(applicationContext).climbDao()

        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ClimbViewModel(dao) as T
            }
        }).get(ClimbViewModel::class.java)


        // get the climbId and load in to view
        val climbId = intent.getIntExtra("CLIMB_ID", -1)
        if (climbId != -1) {
            viewModel.getClimbById(climbId).observe(this) { climb ->
                climb?.let {
                    name.text = it.name
                    grade.text = getString(R.string.txt_grade, it.grade)
                    location.text = getString(R.string.txt_location, it.location)
                    status.text = getString(R.string.txt_status, it.status)
                    notes.text = if (it.notes.isNullOrBlank()) {
                        "Notes: None"
                    } else {
                        getString(R.string.txt_notes, it.notes)
                    }

                    // Display image if available
                    it.imageUri?.let { uriStr ->
                        val file = File(uriStr)
                        if (file.exists()) {
                            image.setImageURI(Uri.fromFile(file))
                        } else {
                            image.visibility = View.GONE
                        }
                    }

                    // Show video button if available
                    it.videoUri?.let { uriStr ->
                        val file = File(uriStr)
                        if (file.exists()) {
                            viewVideoBtn.visibility = View.VISIBLE
                            viewVideoBtn.setOnClickListener {
                                val intent = Intent(this@ViewClimb, VideoPlayerActivity::class.java)
                                intent.putExtra("VIDEO_PATH", uriStr)
                                startActivity(intent)
                            }
                        } else {
                            viewVideoBtn.visibility = View.GONE
                        }
                    }


                    deleteBtn.setOnClickListener {
                        AlertDialog.Builder(this)
                            .setTitle("Delete Climb")
                            .setMessage("Are you sure you want to delete this climb?")
                            .setPositiveButton("Yes") { _, _ ->
                                lifecycleScope.launch {
                                    viewModel.deleteClimb(climb)
                                    finish()
                                }
                            }
                            .setNegativeButton("Cancel", null)
                            .show()
                    }
                }
            }
        }
    }
}
