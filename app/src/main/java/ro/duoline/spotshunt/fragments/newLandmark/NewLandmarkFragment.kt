package ro.duoline.spotshunt.fragments.newLandmark

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.database.LandmarkDatabase
import ro.duoline.spotshunt.databinding.NewLandmarkFragmentBinding

class NewLandmarkFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = NewLandmarkFragment()
    }

    private lateinit var viewModel: NewLandmarkViewModel
    private lateinit var binding: NewLandmarkFragmentBinding
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewLandmarkFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val application = requireNotNull(activity).application
        val dataSource = LandmarkDatabase.getInstance(application).landmarkDatabaseDao
        val viewModelFactory = NewLandmarkModelFactory(application, dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewLandmarkViewModel::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
        centerCamera()
    }

    private fun centerCamera() {
        val latLng = NewLandmarkFragmentArgs.fromBundle(arguments!!).latLng
        val zoom = NewLandmarkFragmentArgs.fromBundle(arguments!!).zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }
}
