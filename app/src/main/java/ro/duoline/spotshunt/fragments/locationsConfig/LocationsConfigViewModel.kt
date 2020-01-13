package ro.duoline.spotshunt.fragments.locationsConfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ro.duoline.spotshunt.fragments.FirebaseUserLiveData
import ro.duoline.spotshunt.fragments.login.LogInViewModel
import ro.duoline.spotshunt.fragments.mainConfig.MainConfigViewModel

class LocationsConfigViewModel : ViewModel() {
    val authenticationState = FirebaseUserLiveData()
        .map { user ->
            if (user != null){
                LogInViewModel.AuthenticationState.AUTHENTICATED
            } else {
                LogInViewModel.AuthenticationState.UNAUTHENTICATED
            }
        }


}
