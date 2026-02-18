// Database for timers.

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import DefaultRoutines

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
                )
                    .addCallback(object : RoomDatabase.Callback() { // Populating the database on creation with the defaults.

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Insert default routines on first creation
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context)
                                    .TimerRoutineDao()
                                    .insertAll(DefaultRoutines.get())
                            }
                        }

                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}