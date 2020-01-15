package ro.duoline.spotshunt.fragments.locationsConfig

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.databinding.LocationsConfigFragmentBinding
import ro.duoline.spotshunt.fragments.login.LogInViewModel

class LocationsConfigFragment : Fragment() , OnMapReadyCallback {

    companion object {
        const val TAG = "LocationsConfigFragment"
        private const val MY_LOCATION_REQUEST_CODE = 119
        fun newInstance() = LocationsConfigFragment()
    }

    private val runningQorLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private lateinit var viewModel: LocationsConfigViewModel
    private lateinit var binding: LocationsConfigFragmentBinding
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationsConfigFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        binding.currentLocation.visibility = View.GONE
        binding.newSpot.visibility = View.GONE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
        //centerCamera()
    }

    /*
    *  Determines whether the app has the appropriate permissions across Android 10+ and all other
    *  Android versions.
    */
    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                )
        val backgroundLocationApproved =
            if (runningQorLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else
                true
        return foregroundLocationApproved && backgroundLocationApproved
    }

//    private fun centerCamera() {
//        val latLng = intent.extras.get(EXTRA_LAT_LNG) as LatLng
//        val zoom = intent.extras.get(EXTRA_ZOOM) as Float
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
//    }
}
