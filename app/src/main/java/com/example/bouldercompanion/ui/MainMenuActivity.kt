package com.example.bouldercompanion.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bouldercompanion.R

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val viewClimbListBtn = findViewById<Button>(R.id.climbListButton)
        val trainingBtn = findViewById<Button>(R.id.trainingButton)

        viewClimbListBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        trainingBtn.setOnClickListener {
            val intent = Intent(this, TimerListActivity::class.java)
            startActivity(intent)
        }
    }
}

