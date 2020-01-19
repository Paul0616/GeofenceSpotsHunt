package ro.duoline.spotshunt.fragments.newLandmark

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ro.duoline.spotshunt.database.LandmarkDatabaseDao
import ro.duoline.spotshunt.models.LandmarkDataObject
import ro.duoline.spotshunt.repository.LandmarkRepository

class NewLandmarkViewModel(app: Application, val database: LandmarkDatabaseDao) :
    AndroidViewModel(app) {
    private val landmarksRepository = LandmarkRepository(app, database)
    private var viewModelJob = Job()
    private var scope = CoroutineScope(Dispatchers.Default + viewModelJob)

    private val _navigateBack = MutableLiveData<Boolean>(false)
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack


    fun navigationComplete(){
        _navigateBack.value = false
    }

    fun insertLandmark(landmark: LandmarkDataObject) {
        scope.launch {
            landmarksRepository.insertLandmark(landmark)
            _navigateBack.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
