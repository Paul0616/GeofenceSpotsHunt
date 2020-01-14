package ro.duoline.spotshunt.fragments.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.databinding.LogInFragmentBinding
import ro.duoline.spotshunt.fragments.mainConfig.MainConfigFragment

class LogInFragment : Fragment() {

    companion object {
        const val TAG = "LogInFragment"
        const val SIGN_IN_RESULT_CODE = 1001
        fun newInstance() = LogInFragment()
    }

    private lateinit var viewModel: LogInViewModel
    private lateinit var binding: LogInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LogInFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        // If the user presses the back button, bring them back to the home screen.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.mainConfig, false)
        }

        viewModel.authenticationState.observe(this, Observer{
            when(it){
                LogInViewModel.AuthenticationState.AUTHENTICATED -> findNavController().popBackStack()
                LogInViewModel.AuthenticationState.INVALID_AUTHENTICATION -> Snackbar.make(
                    view, requireActivity().getString(R.string.login_unsuccessful_msg),
                    Snackbar.LENGTH_LONG
                ).show()
                else -> Log.e(
                    TAG,
                    "Authentication state that doesn't require any UI change $it"
                )
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        binding.authButton.setOnClickListener { launchSignInFlow() }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
            LogInFragment.SIGN_IN_RESULT_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LogInFragment.SIGN_IN_RESULT_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK){
                Log.i(MainConfigFragment.TAG, "Successfully signed in user " + "${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(MainConfigFragment.TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
