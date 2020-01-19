package ro.duoline.spotshunt.fragments.locationsConfig

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import ro.duoline.spotshunt.BuildConfig

import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.database.LandmarkDatabase
import ro.duoline.spotshunt.databinding.LocationsConfigFragmentBinding
import ro.duoline.spotshunt.fragments.login.LogInViewModel
import ro.duoline.spotshunt.models.LandmarkDataObject
import ro.duoline.spotshunt.models.showLandmarkInMap

class LocationsConfigFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        const val TAG = "LocationsConfigFragment"
        private const val FOREGROUNG_ONLY_PERMISSION_REQUEST_CODE = 119
        private const val FOREGROUNG_AND_BACKGROUND_PERMISSION_REQUEST_CODE = 120
        fun newInstance() = LocationsConfigFragment()
    }

    private val runningQorLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private lateinit var viewModel: LocationsConfigViewModel
    private lateinit var binding: LocationsConfigFragmentBinding
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager
    private var landmarksSize: Int? = null

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
        val application = requireNotNull(activity).application
        val dataSource = LandmarkDatabase.getInstance(application).landmarkDatabaseDao
        val viewModelFactory = LocationsConfigModelFactory(application, dataSource)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(LocationsConfigViewModel::class.java)
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
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }


    private fun checkPermissionForLocation() {
        if (foregroundAndBackgroundLocationPermissionApproved()) {
            Log.i(TAG, "start device location")
            startDeviceLocation()
        } else {
            requestForegraundAndBackgroundPermissions()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = false
        map.setOnMarkerClickListener(this)
        checkPermissionForLocation()

    }

    private fun startDeviceLocation() {
        Log.i(TAG, "Location tracking will be started here")
        map.isMyLocationEnabled = true
        binding.currentLocation.visibility = View.VISIBLE
        binding.newSpot.visibility = View.VISIBLE

        binding.currentLocation.setOnClickListener {
            val bestProvider = locationManager.getBestProvider(Criteria(), false)
            Log.i(TAG, bestProvider)
            if (foregroundAndBackgroundLocationPermissionApproved()) {
                val location = locationManager.getLastKnownLocation(bestProvider)
                Log.i(TAG, "$location")
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    centerCamera(latLng)

                }
            }
        }
        binding.newSpot.setOnClickListener {
            if (landmarksSize != null) {
                val currentIndex = landmarksSize!! + 1
                map?.run {
                    findNavController().navigate(
                        LocationsConfigFragmentDirections
                            .actionLocationsConfigFragmentToNewLandmarkFragment(
                                cameraPosition.target,
                                cameraPosition.zoom,
                                currentIndex
                            )
                    )
                }
            }
        }

        viewModel.landmarks.observe(this, Observer {
            if (it != null) {
                showLandmarks(it)
                if (it.size > 0) {
                    val lastLandmark = it.last()
                    if (lastLandmark.latitude != null && lastLandmark.longitude != null) {
                        val lastLatLng = LatLng(lastLandmark.latitude!!, lastLandmark.longitude!!)
                        centerCamera(lastLatLng)
                    }

                }
            }
        })
    }

    private fun centerCamera(latLng: LatLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun showLandmarks(landmarks: List<LandmarkDataObject>) {
        landmarksSize = landmarks.size
        Log.i(TAG, "${landmarksSize} landmarks")

        map?.run {
            clear()
            var index = 0
            for (landmark in landmarks) {
                index++
                showLandmarkInMap(context!!, index.toString(), this, landmark)
            }
        }
    }


    /*
    *  Determines whether the app has the appropriate permissions across Android 10+ and all other
    *  Android versions.
    */
    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
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

    /*
    *  Requests ACCESS_FINE_LOCATION and (on Android 10+ (Q) ACCESS_BACKGROUND_LOCATION.
    */
    @TargetApi(29)
    private fun requestForegraundAndBackgroundPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved()) return

        var permissionArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val requestCode = when {
            runningQorLater -> {
                permissionArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                FOREGROUNG_AND_BACKGROUND_PERMISSION_REQUEST_CODE
            }
            else -> FOREGROUNG_ONLY_PERMISSION_REQUEST_CODE
        }
        Log.i(TAG, "permission must be requested")
        requestPermissions(permissionArray, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty()
            || grantResults[0] == PackageManager.PERMISSION_DENIED
            || (requestCode == FOREGROUNG_AND_BACKGROUND_PERMISSION_REQUEST_CODE
                    && grantResults[1] == PackageManager.PERMISSION_DENIED)
        ) {
            Snackbar.make(
                binding.main,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction("Settings") {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
                .show()
        } else {
            startDeviceLocation()
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        Log.i(TAG, "${marker?.tag}")
        return true
    }

}
