package ro.duoline.spotshunt.repository

import android.content.Context
import androidx.lifecycle.LiveData
import ro.duoline.spotshunt.database.LandmarkDatabaseDao
import ro.duoline.spotshunt.models.LandmarkDataObject

class LandmarkRepository(private val context: Context, private val database: LandmarkDatabaseDao) {


    fun getSize(): Int{
        return database.getSize()
    }

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
}