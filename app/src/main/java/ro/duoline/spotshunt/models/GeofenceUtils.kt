package ro.duoline.spotshunt.models

import com.google.android.gms.maps.model.LatLng
import java.util.*
import java.util.concurrent.TimeUnit

data class LandmarkDataObject(
    val id: String = UUID.randomUUID().toString(),
    val hint: String?,
    val name: String?,
    val radius: Double?,
    val latLong: LatLng?)

internal object GeofencingConstants {
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.DAYS.toMillis(1)
}