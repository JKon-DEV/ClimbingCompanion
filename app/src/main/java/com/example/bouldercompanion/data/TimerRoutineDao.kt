import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimerRoutineDao {
    @Query("SELECT * FROM timers")
    fun getAllFlow(): kotlinx.coroutines.flow.Flow<List<TimerRoutine>>

    @Insert
    suspend fun insertAll(vararg timers: TimerRoutine)

    @Insert
    suspend fun insert(timer: TimerRoutine)

    @Delete
    suspend fun delete(timer: TimerRoutine)

    @Update
    suspend fun update(timer: TimerRoutine)

}
