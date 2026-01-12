import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timers")
data class TimerRoutine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val sets: Int,
    val repsPerSet: Int,
    val restBetweenSets: Int,   // seconds
    val restBetweenReps: Int,   // seconds
    val hangTime: Int           // seconds
)
