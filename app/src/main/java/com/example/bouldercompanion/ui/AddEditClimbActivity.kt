package com.example.bouldercompanion.ui

import Climb
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bouldercompanion.R
import ClimbDatabase
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bouldercompanion.viewmodel.ClimbViewModel
import java.io.File

class AddEditClimbActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var gradeInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var statusInput: EditText
    private lateinit var notesInput: EditText
    private lateinit var btnImage: Button
    private lateinit var btnVideo: Button
    private lateinit var btnSave: Button

    private var imageUri: String? = null
    private var videoUri: String? = null
    private var climbId: Int? = null

    // register gallery pickers
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val localPath = copyUriToInternalStorage(it)
                if (localPath != null) {
                    imageUri = localPath
                } else {
                    Toast.makeText(this, "Failed to copy image", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val pickVideoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val localPath = copyUriToInternalStorage(it)
            if (localPath != null) {
                videoUri = localPath
            } else {
                Toast.makeText(this, "Failed to copy video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to copy media to internal storage and return the correct path for later use.
    private fun copyUriToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val fileName = "climb_${System.currentTimeMillis()}.jpg"
            val file = File(filesDir, fileName)

            // Copy the data into app-private storage
            file.outputStream().use { output ->
                inputStream.copyTo(output)
            }

            file.absolutePath   // return the path as a String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_climb)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // construct dao
        val dao = ClimbDatabase.getDatabase(applicationContext).climbDao()

        // Construct our viewmodel
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ClimbViewModel(dao) as T
            }
        }).get(ClimbViewModel::class.java)

        nameInput = findViewById(R.id.inputName)
        gradeInput = findViewById(R.id.inputGrade)
        locationInput = findViewById(R.id.inputLocation)
        statusInput = findViewById(R.id.inputStatus)
        notesInput = findViewById(R.id.inputNotes)
        btnImage = findViewById(R.id.btnPickImage)
        btnVideo = findViewById(R.id.btnPickVideo)
        btnSave = findViewById(R.id.btnSaveTimer)

        // getting id of climb to display if editing
        climbId = intent.getIntExtra("CLIMB_ID", -1).takeIf { it != -1 }

        // if editing, load data
        if (climbId != null) {
            viewModel.getClimbById(climbId!!).observe(this) { climb ->
                climb?.let {
                    nameInput.setText(it.name)
                    gradeInput.setText(it.grade)
                    locationInput.setText(it.location)
                    statusInput.setText(it.status)
                    notesInput.setText(it.notes)
                    imageUri = (it.imageUri)
                    videoUri = (it.videoUri)
                }
            }
        }

        // image and video pickers
        btnImage.setOnClickListener { pickImageLauncher.launch("image/*") }
        btnVideo.setOnClickListener { pickVideoLauncher.launch("video/*") }


        // Save button, adds climb or updates climb depending on if we are editing or adding
        btnSave.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val grade = gradeInput.text.toString().trim()
            val location = locationInput.text.toString().trim()
            val status = statusInput.text.toString().trim()
            val notes = notesInput.text.toString().trim()

            if (name.isEmpty() || grade.isEmpty() || location.isEmpty() || status.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // if climbId is null obviously we are adding new climb
            if (climbId == null) {
                viewModel.addClimb(
                    Climb(
                        name = name,
                        grade = grade,
                        location = location,
                        status = status,
                        notes = notes.ifEmpty { null },
                        imageUri = imageUri,
                        videoUri = videoUri
                    )
                )
            } else {
                // else we will be updating, climbId stays the name, otherwise update other fields if needed to data goes through
                val updated = Climb(
                    id = climbId!!,
                    name = name,
                    grade = grade,
                    location = location,
                    status = status,
                    notes = notes.ifEmpty { null },
                    imageUri = imageUri,
                    videoUri = videoUri
                )
                viewModel.updateClimb(updated)
            }

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}

