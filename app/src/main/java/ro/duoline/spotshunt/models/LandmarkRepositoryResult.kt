package ro.duoline.spotshunt.models

import androidx.lifecycle.LiveData

data class LandmarkRepositoryResult(
    val landmarks: LiveData<List<LandmarkDataObject>>,
    val errors: LiveData<String>
)