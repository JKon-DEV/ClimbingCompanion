package com.example.bouldercompanion.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bouldercompanion.R
import java.io.File

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var topBar: View
    private lateinit var playOverlay: ImageView
    private lateinit var btnBack : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        videoView = findViewById(R.id.videoView)
        topBar = findViewById(R.id.topBar)
        playOverlay = findViewById(R.id.playPauseOverlay)
        btnBack = findViewById<ImageButton>(R.id.btnBack)

        val path = intent.getStringExtra("VIDEO_PATH") ?: return
        val file = File(path)
        if (!file.exists()) return

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.fromFile(file))
        videoView.start()

        // Tap anywhere on video to toggle the top bar and overlay
        videoView.setOnClickListener {
            if (topBar.visibility == View.VISIBLE) {
                topBar.visibility = View.GONE
            } else {
                topBar.visibility = View.VISIBLE
            }
        }

        btnBack.setOnClickListener { // Button is hard to press sometimes
            finish()
        }
    }
}

