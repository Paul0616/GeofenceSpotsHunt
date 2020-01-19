package ro.duoline.spotshunt.fragments.newLandmark

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

import ro.duoline.spotshunt.R
import ro.duoline.spotshunt.database.LandmarkDatabase
import ro.duoline.spotshunt.databinding.NewLandmarkFragmentBinding
import ro.duoline.spotshunt.models.LandmarkDataObject
import ro.duoline.spotshunt.models.showLandmarkInMap
import kotlin.math.roundToInt

class NewLandmarkFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val TAG = "NewLandmarkFragment"
        fun newInstance() = NewLandmarkFragment()
    }

    private lateinit var viewModel: NewLandmarkViewModel
    private lateinit var binding: NewLandmarkFragmentBinding
    private lateinit var map: GoogleMap
    private var currentIndex: Int? = null

    private var landmark = LandmarkDataObject(
        hint = null,
        name = null,
        radius = null,
        latitude = null,
        longitude = null
    )

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
        binding.marker.visibility = View.GONE
        binding.instrictionTitle.visibility = View.GONE
        binding.instructionSubtitle.visibility = View.GONE
        binding.radiusBar.visibility = View.GONE
        binding.radiusDescription.visibility = View.GONE
        binding.landmarkName.visibility = View.GONE
        binding.landmarkHint.visibility = View.GONE

        viewModel.navigateBack.observe(this, Observer {
            if (it) {
                findNavController().navigate(
                    NewLandmarkFragmentDirections.actionNewLandmarkFragmentToLocationsConfigFragment()
                )
                viewModel.navigationComplete()
            }
        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false
        centerCamera()
        showConfigureLocationStep()
    }

    private fun showConfigureLocationStep() {
        binding.marker.visibility = View.VISIBLE
        binding.instrictionTitle.visibility = View.VISIBLE
        binding.instructionSubtitle.visibility = View.VISIBLE
        binding.radiusBar.visibility = View.GONE
        binding.radiusDescription.visibility = View.GONE
        binding.landmarkName.visibility = View.GONE
        binding.landmarkHint.visibility = View.GONE
        binding.instrictionTitle.text = getString(R.string.instruction_location_title)
        binding.instructionSubtitle.text = getString(R.string.instruction_subtitle)
        binding.next.setOnClickListener {
            landmark.latitude = map.cameraPosition.target.latitude
            landmark.longitude = map.cameraPosition.target.longitude
            showConfigureRadiusSetup()
        }
        showLandmarkUpdate()
    }


    private fun showConfigureRadiusSetup() {
        binding.marker.visibility = View.GONE
        binding.instrictionTitle.visibility = View.VISIBLE
        binding.instructionSubtitle.visibility = View.GONE
        binding.radiusBar.visibility = View.VISIBLE
        binding.radiusDescription.visibility = View.VISIBLE
        binding.landmarkName.visibility = View.GONE
        binding.landmarkHint.visibility = View.GONE
        binding.instrictionTitle.text = getString(R.string.instruction_radius_title)
        binding.next.setOnClickListener {
            showConfigureNameAndHintSetup()
        }
        binding.radiusBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateRadiusWithProgress(progress)
                showLandmarkUpdate()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        updateRadiusWithProgress(binding.radiusBar.progress)
        map.animateCamera(CameraUpdateFactory.zoomTo(15f))
        showLandmarkUpdate()
    }

    private fun updateRadiusWithProgress(progress: Int) {
        val radius = getRadius(progress)
        landmark.radius = radius
        binding.radiusDescription.text =
            getString(R.string.radius_description, radius.roundToInt().toString())
    }

    private fun getRadius(progress: Int): Double = 100 + progress.toDouble() * 100

    private fun showConfigureNameAndHintSetup() {
        binding.marker.visibility = View.GONE
        binding.instrictionTitle.visibility = View.VISIBLE
        binding.instructionSubtitle.visibility = View.GONE
        binding.radiusBar.visibility = View.GONE
        binding.radiusDescription.visibility = View.GONE
        binding.landmarkName.visibility = View.VISIBLE
        binding.landmarkHint.visibility = View.VISIBLE
        binding.instrictionTitle.text = getString(R.string.instruction_mesage_title)
        binding.next.setOnClickListener {
            landmark.name = binding.landmarkName.text.toString()
            landmark.hint = binding.landmarkHint.text.toString()
            Log.i(TAG, "$landmark")
            viewModel.insertLandmark(landmark)
        }
        showLandmarkUpdate()
    }

    private fun centerCamera() {
        val latLng = NewLandmarkFragmentArgs.fromBundle(arguments!!).latLng
        val zoom = NewLandmarkFragmentArgs.fromBundle(arguments!!).zoom
        currentIndex = NewLandmarkFragmentArgs.fromBundle(arguments!!).currentIndex
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun showLandmarkUpdate() {
        map.clear()
        showLandmarkInMap(context!!, currentIndex.toString(), map, landmark)
    }
}
