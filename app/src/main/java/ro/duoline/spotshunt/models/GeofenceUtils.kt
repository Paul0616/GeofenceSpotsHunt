package ro.duoline.spotshunt.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import org.jetbrains.annotations.NotNull
import ro.duoline.spotshunt.R
import java.util.*
import java.util.concurrent.TimeUnit


@Entity(tableName = "landmarks_table")
data class LandmarkDataObject(
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "hint")
    var hint: String?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "radius")
    var radius: Double?,

    @ColumnInfo(name = "latitude")
    var latitude: Double?,

    @ColumnInfo(name = "longitude")
    var longitude: Double?,

    @ColumnInfo(name = "wasReached")
    var wasReached: Boolean = false,

    @ColumnInfo(name = "createdTime")
    val createdTime : Long = System.currentTimeMillis()
)


fun showLandmarkInMap(context: Context, text: String, map: GoogleMap, landmark: LandmarkDataObject){
    val iconFactory = IconGenerator(context)

    if(landmark.latitude != null && landmark.longitude != null){
        val lat = landmark.latitude as Double
        val lng = landmark.longitude as Double
        val latLng = LatLng(lat, lng)

        val markerOptions = MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
            .position(latLng)
            .anchor(iconFactory.anchorU, iconFactory.anchorV)

        val marker = map.addMarker(markerOptions)
        marker.tag = landmark.id
        if(landmark.radius != null) {
            val radius = landmark.radius as Double
            map.addCircle(CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(ContextCompat.getColor(context, R.color.colorAccent))
                .fillColor(ContextCompat.getColor(context, R.color.colorLandmarkFill))
            )
        }
    }
}


internal object GeofencingConstants {
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.DAYS.toMillis(1)
}