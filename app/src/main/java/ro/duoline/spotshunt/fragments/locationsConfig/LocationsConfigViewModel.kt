package ro.duoline.spotshunt.fragments.locationsConfig

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.duoline.spotshunt.database.LandmarkDatabaseDao
import ro.duoline.spotshunt.fragments.FirebaseUserLiveData
import ro.duoline.spotshunt.fragments.login.LogInViewModel
import ro.duoline.spotshunt.models.LandmarkDataObject
import ro.duoline.spotshunt.models.LandmarkRepositoryResult
import ro.duoline.spotshunt.repository.LandmarkRepository

class LocationsConfigViewModel(app: Application, val database: LandmarkDatabaseDao) :
    AndroidViewModel(app) {

    private val landmarksRepository = LandmarkRepository(app, database)
    private val startGetLandmarks = MutableLiveData<Boolean>(false)

    //private val landmarksRepositoryResponse: LiveData<List>

    private var landmarksResult = Transformations.map(startGetLandmarks){
        landmarksRepository.getUntouchedLandmarks()
    }


    var landmarks = Transformations.switchMap(landmarksResult){
        it.landmarks
    }

    val authenticationState = FirebaseUserLiveData()
        .map { user ->
            if (user != null) {
                LogInViewModel.AuthenticationState.AUTHENTICATED
            } else {
                LogInViewModel.AuthenticationState.UNAUTHENTICATED
            }
        }

    fun showLandmarks() {
        startGetLandmarks.postValue(true)
    }
    fun resetShowLandmarks() {
        startGetLandmarks.postValue(false)
    }

//    fun loadLandmarks(){
//        viewModelScope.launch {
//            landmarks = landmarksRepository.getUntouchedLandmarks()
//        }
//    }

    override fun onCleared() {

        super.onCleared()
    }
}
