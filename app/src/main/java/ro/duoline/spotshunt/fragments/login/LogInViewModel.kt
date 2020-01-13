package ro.duoline.spotshunt.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ro.duoline.spotshunt.fragments.FirebaseUserLiveData

class LogInViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val authenticationState = FirebaseUserLiveData()
        .map { user ->
            if (user != null){
                AuthenticationState.AUTHENTICATED
            } else {
                AuthenticationState.UNAUTHENTICATED
            }

        }
}
