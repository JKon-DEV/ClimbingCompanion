// Setting up room database to hold the climb objects.

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Climb::class], version = 1)
abstract class ClimbDatabase : RoomDatabase() {
    abstract fun climbDao(): ClimbDao

    companion object {
        @Volatile private var INSTANCE: ClimbDatabase? = null

        fun getDatabase(context: Context): ClimbDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClimbDatabase::class.java,
                    "climb_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
