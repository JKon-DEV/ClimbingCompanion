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
    val imageUri: String? = null,   // Will need to copy this to app private storage so photo doesn't disappear if user deletes...
    val videoUri: String? = null
)

