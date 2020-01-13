package ro.duoline.spotshunt.fragments.locationsConfig

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.databinding.LocationsConfigFragmentBinding
import ro.duoline.spotshunt.fragments.login.LogInViewModel

class LocationsConfigFragment : Fragment() {

    companion object {
        const val TAG = "LocationsConfigFragment"
        fun newInstance() = LocationsConfigFragment()
    }

    private lateinit var viewModel: LocationsConfigViewModel
    private lateinit var binding: LocationsConfigFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationsConfigFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationsConfigViewModel::class.java)
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LogInViewModel.AuthenticationState.AUTHENTICATED -> Log.i(TAG, "Authenticated")
                // If the user is not logged in, they should not be able to set any preferences,
                // so navigate them to the login fragment
                LogInViewModel.AuthenticationState.UNAUTHENTICATED -> findNavController().navigate(
                    R.id.logInFragment
                )
                else -> Log.e(
                    TAG, "New $authenticationState state that doesn't require any UI change"
                )
            }
        })
    }

}
