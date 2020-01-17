package ro.duoline.spotshunt.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ro.duoline.spotshunt.database.LandmarkDatabaseDao
import ro.duoline.spotshunt.models.LandmarkDataObject
import ro.duoline.spotshunt.models.LandmarkRepositoryResult
import java.util.concurrent.TimeUnit

class LandmarkRepository(private val context: Context, private val database: LandmarkDatabaseDao) {
    private val error = MutableLiveData<String>()



    fun add(landmark: LandmarkDataObject){
        val currentLandmark: LiveData<LandmarkDataObject?>  = database.getCurrentLandmark()

        //return untouchedLandmarks
    }

    fun insertLandmark(landmark: LandmarkDataObject) {
        database.insertLandmark(landmark)
    }

    fun getLandmarkById(id: String): LandmarkDataObject?{
       return database.getLandmarkById(id)
    }

    fun getUntouchedLandmarks() : LandmarkRepositoryResult {
        return LandmarkRepositoryResult(
            database.getUntouchedLandmarks(),
            error
        )
    }

    suspend fun makeCallAndUpdateLandmarks() {
        withContext(Dispatchers.IO){
            val touchedLandmarks = database.getTouchedSize()
            if (touchedLandmarks == 0){
                /*TODO: network api call getLandmarks() and we get List<LandmarkDataObject>
                  then insert new landmarks
                */
                //save in database
                //val inserted = database.insertLandmarks(landmarks)

                //delete obsolete landmarks from database (which no longer exists in the network API)
//                val expireTime = TimeUnit.MINUTES.toMillis(15)
//                val oldLandmarks = database.getOldLandmarks(System.currentTimeMillis() - expireTime)
//                database.deleteOldLandmarks(oldLandmarks)
            }
        }
    }

}