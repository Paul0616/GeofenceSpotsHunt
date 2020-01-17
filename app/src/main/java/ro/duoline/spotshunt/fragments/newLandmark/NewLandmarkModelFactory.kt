package ro.duoline.spotshunt.fragments.newLandmark

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ro.duoline.spotshunt.database.LandmarkDatabaseDao

class NewLandmarkModelFactory(private val application: Application, private val dataSource: LandmarkDatabaseDao): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(NewLandmarkViewModel::class.java)){
           return NewLandmarkViewModel(application, dataSource) as T
       }
        throw java.lang.IllegalAccessException("Unknown ViewModel class")
    }
}