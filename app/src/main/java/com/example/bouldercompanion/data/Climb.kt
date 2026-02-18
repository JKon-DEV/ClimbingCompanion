// Climb class definition and metadata it stores

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "climbs")
data class Climb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val grade: String,
    val location: String,
    val status: String,
    val notes: String?,
    val imageUri: String? = null,
    val videoUri: String? = null
)

