package ro.duoline.spotshunt.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.concurrent.TimeUnit

@Entity(tableName = "landmarks_table")
data class LandmarkDataObject(
    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "hint")
    val hint: String?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "radius")
    val radius: Double?,

    @ColumnInfo(name = "latitude")
    val latitude: Double?,

    @ColumnInfo(name = "longitude")
    val longitude: Double?,

    @ColumnInfo(name = "wasReached")
    val wasReached: Boolean = false,

    @ColumnInfo(name = "createdTime")
    val createdTime : Long = System.currentTimeMillis()
) {

    val latLng: LatLng? = if (latitude != null && longitude != null) {
        LatLng(latitude, longitude)
    } else
        null
}


internal object GeofencingConstants {
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.DAYS.toMillis(1)
}