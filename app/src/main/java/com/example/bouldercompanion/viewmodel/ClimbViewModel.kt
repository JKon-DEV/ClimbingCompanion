package com.example.bouldercompanion.viewmodel

import Climb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import ClimbDao
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.launch
import java.io.File

class ClimbViewModel(private val dao: ClimbDao) : ViewModel() {
// new class of type viewmodel, taking Dao in the constructor so it can access and update the database and reflect changes in UI
    private val searchQuery = MutableLiveData("")

    val climbs = searchQuery.switchMap { query ->
        if (query.isBlank()) {
            dao.getAllFlow().asLiveData()
        } else {
            dao.searchClimbs(query).asLiveData()
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun addClimb(climb: Climb) = viewModelScope.launch {
        dao.insert(climb)
    }

    fun updateClimb(climb: Climb) = viewModelScope.launch {
        dao.update(climb)
    }

    fun deleteClimb(climb: Climb) = viewModelScope.launch {
        dao.delete(climb)

        climb.imageUri?.let { deleteFileIfExists(it) }
        climb.videoUri?.let { deleteFileIfExists(it) }
    }

    fun getClimbById(id: Int) = dao.getClimbByIdFlow(id).asLiveData()

    private fun deleteFileIfExists(path: String) {
        try {
            val file = File(path)
            if (file.exists()) {
                val deleted = file.delete()
                Log.d("ClimbViewModel", "Deleted file: $deleted | Path: $path")
            } else {
                Log.d("ClimbViewModel", "File not found: $path")
            }
        } catch (e: Exception) {
            Log.e("ClimbViewModel", "Error deleting file: ${e.message}")
        }
    }
}
