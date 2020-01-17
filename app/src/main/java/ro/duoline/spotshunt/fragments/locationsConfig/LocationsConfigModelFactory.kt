package ro.duoline.spotshunt.fragments.locationsConfig

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.duoline.spotshunt.database.LandmarkDatabaseDao

class LocationsConfigModelFactory(private val application: Application, private val dataSource: LandmarkDatabaseDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(LocationsConfigViewModel::class.java)){
           return LocationsConfigViewModel(application, dataSource) as T
       }
        throw java.lang.IllegalAccessException("Unknown ViewModel class")
    }
}