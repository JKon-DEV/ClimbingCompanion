package com.example.bouldercompanion.ui

import ClimbDao
import ClimbDatabase
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bouldercompanion.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModel
import com.example.bouldercompanion.viewmodel.ClimbViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private lateinit var addClimbButton: FloatingActionButton
private lateinit var dao: ClimbDao
private lateinit var climbList: RecyclerView
private lateinit var adapter: ClimbAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_climb_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addClimbButton = findViewById<FloatingActionButton>(R.id.addClimbButton)
        climbList = findViewById<RecyclerView>(R.id.climbList)
        climbList.layoutManager = LinearLayoutManager(this)

        dao = ClimbDatabase.getDatabase(this).climbDao()

        // Create our viewmodel, cant do this the normal way because our viewmodel needs dao argument
        val climbViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // Casting as T is generic type
                return ClimbViewModel(dao) as T
            }
        }).get(ClimbViewModel::class.java)

        // Construct new climbAdapter
        adapter = ClimbAdapter(
            emptyList(),
            onClick = { climb ->
                val intent = Intent(this, ViewClimb::class.java)
                intent.putExtra("CLIMB_ID", climb.id)
                startActivity(intent)
            },
            onLongClick = { climb ->
                val intent = Intent(this, AddEditClimbActivity::class.java)
                intent.putExtra("CLIMB_ID", climb.id)
                startActivity(intent)
            }
        )

        // Connect recyclerview with adapter
        climbList.adapter = adapter


        // Update recyclerview with climbs
        climbViewModel.climbs.observe(this) { climbs ->
            adapter.updateData(climbs)
        }

        addClimbButton.setOnClickListener {
            val intent = Intent(this, AddEditClimbActivity::class.java)
            startActivity(intent)
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.isIconifiedByDefault = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                climbViewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                climbViewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }
}

