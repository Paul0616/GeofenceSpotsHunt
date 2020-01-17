package ro.duoline.spotshunt.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ro.duoline.spotshunt.models.LandmarkDataObject

@Dao
interface LandmarkDatabaseDao {
    @Insert
    fun insertLandmark(landmark: LandmarkDataObject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLandmarks(post: List<LandmarkDataObject>): List<Long>

    @Query("SELECT COUNT(id) FROM landmarks_table WHERE wasReached = 1")
    fun getTouchedSize(): Int

    @Query("SELECT * FROM landmarks_table WHERE wasReached = 0 ORDER BY createdTime")
    fun getUntouchedLandmarks(): LiveData<List<LandmarkDataObject>>

    @Query("SELECT * FROM landmarks_table WHERE id = :id LIMIT 1")
    fun getLandmarkById(id: String): LandmarkDataObject?

    @Query("SELECT * FROM landmarks_table WHERE wasReached = 0 ORDER BY createdTime LIMIT 1")
    fun getCurrentLandmark(): LiveData<LandmarkDataObject?>

}