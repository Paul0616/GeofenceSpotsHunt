package ro.duoline.spotshunt.fragments.mainConfig

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.databinding.MainConfigFragmentBinding
import ro.duoline.spotshunt.fragments.login.LogInFragment
import ro.duoline.spotshunt.fragments.login.LogInViewModel


class MainConfigFragment : Fragment() {

    companion object {
        const val TAG = "MainConfigFragment"
        fun newInstance() = MainConfigFragment()
    }

    private lateinit var viewModel: MainConfigViewModel
    private lateinit var binding: MainConfigFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainConfigFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainConfigViewModel::class.java)

        binding.configLocationsButton.setOnClickListener {
            findNavController().navigate(MainConfigFragmentDirections.actionLogInToLocationsConfigFragment())
        }

        binding.authButton.text = getString(R.string.logout_btn)
        binding.authButton.setOnClickListener {
            AuthUI.getInstance().signOut(requireContext())
        }

        viewModel.authenticationState.observe(this, Observer{
            when(it){
                LogInViewModel.AuthenticationState.AUTHENTICATED -> {
                    Log.i(TAG, "LOGAT")
                    binding.authButton.visibility = View.VISIBLE
                }
                LogInViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    Log.i(TAG, "NELOGAT")
                    binding.authButton.visibility = View.INVISIBLE
                }
            }
        })
    }
}
