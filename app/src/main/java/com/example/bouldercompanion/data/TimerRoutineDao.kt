import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerRoutineDao {
    @Query("SELECT * FROM timers")
    fun getAllFlow(): kotlinx.coroutines.flow.Flow<List<TimerRoutine>>

    @Insert
    suspend fun insertAll(timers: List<TimerRoutine>)

    @Insert
    suspend fun insert(timer: TimerRoutine)

    @Query("SELECT * FROM timers WHERE id = :id")
    fun getRoutineByIdFlow(id: Int): Flow<TimerRoutine?>

    @Delete
    suspend fun delete(timer: TimerRoutine)

    @Update
    suspend fun update(timer: TimerRoutine)

}
