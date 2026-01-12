import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TimerRoutine::class], version = 1)
abstract class TimerRoutineDatabase : RoomDatabase() {
    abstract fun TimerRoutineDao(): TimerRoutineDao

    companion object {
        @Volatile private var INSTANCE: TimerRoutineDatabase? = null

        fun getDatabase(context: Context): TimerRoutineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimerRoutineDatabase::class.java,
                    "timer_routine_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}