package ro.duoline.spotshunt.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/**
   -----------------------------------
     abstract class cannot be instantiated
     so for keeping only one instance we
     need Singleton pattern
   -----------------------------------
*/

abstract class LandmarkDatabase: RoomDatabase() {
    /**
     * Connects the database to the DAO.
     */
    abstract val landmarkDatabaseDao: LandmarkDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: LandmarkDatabase? = null

        fun getInstance(context: Context): LandmarkDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LandmarkDatabase::class.java,
                        "landmark_database"
                    )
                        .fallbackToDestructiveMigration() // Wipes and rebuilds instead of migrating if no Migration object.
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}