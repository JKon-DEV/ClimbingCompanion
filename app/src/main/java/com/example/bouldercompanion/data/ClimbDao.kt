import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClimbDao {
    @Query("SELECT * FROM climbs")
    fun getAllFlow(): kotlinx.coroutines.flow.Flow<List<Climb>>

    @Insert
    suspend fun insertAll(vararg climbs: Climb)

    @Insert
    suspend fun insert(climb: Climb)

    @Delete
    suspend fun delete(climb: Climb)

    @Update
    suspend fun update(climb: Climb)

    @Query("SELECT * FROM climbs WHERE id = :id")
    suspend fun getClimbById(id: Int): Climb?

    @Query("SELECT * FROM climbs WHERE id = :id")
    fun getClimbByIdFlow(id: Int): kotlinx.coroutines.flow.Flow<Climb?>

    @Query("SELECT * FROM climbs WHERE name LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchClimbs(query: String): kotlinx.coroutines.flow.Flow<List<Climb>>
}
