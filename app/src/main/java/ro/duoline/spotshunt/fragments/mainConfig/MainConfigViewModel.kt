package ro.duoline.spotshunt.fragments.mainConfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ro.duoline.spotshunt.fragments.FirebaseUserLiveData
import ro.duoline.spotshunt.fragments.login.LogInViewModel

class MainConfigViewModel : ViewModel() {
    val authenticationState = FirebaseUserLiveData().map { user ->
            if (user != null){
                LogInViewModel.AuthenticationState.AUTHENTICATED
            } else {
                LogInViewModel.AuthenticationState.UNAUTHENTICATED
            }

        }
}
