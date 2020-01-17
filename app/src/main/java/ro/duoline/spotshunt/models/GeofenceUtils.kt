package ro.duoline.spotshunt.models

import android.R.attr
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.Align
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
)

{
    fun getLatLng(): LatLng? = if (latitude != null && longitude != null) {
        LatLng(latitude, longitude)
    } else
        null
}

fun showLandmarkInMap(context: Context, map: GoogleMap, landmark: LandmarkDataObject){
    if(landmark.getLatLng() != null){
        val latLng = landmark.getLatLng() as LatLng
       // val vectorTo
        val bmp = drawTextToBitmap("5", 24f, Color.YELLOW)
        val marker = map.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bmp)))
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

//private fun drawTextToBitmap(context: Context, gResId: Int, textSize: Int = 24, text1: String): Bitmap {
//    val resources = context.resources
//    val scale = resources.displayMetrics.density
//    var bitmap = BitmapFactory.decodeResource(resources, gResId)
//
//    var bitmapConfig = bitmap.config;
//    // set default bitmap config if none
//    if (bitmapConfig == null) {
//        bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888
//    }
//    // resource bitmaps are imutable,
//    // so we need to convert it to mutable one
//    bitmap = bitmap.copy(bitmapConfig, true)
//
//    val canvas = Canvas(bitmap)
//    // new antialised Paint
//    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//    paint.color = Color.rgb(93, 101, 67)
//    // text size in pixels
//    paint.textSize = (textSize * scale).roundToInt().toFloat()
//    //custom fonts
//    //val fontFace = ResourcesCompat.getFont(context, )
//    //paint.typeface = Typeface.create(fontFace, Typeface.NORMAL)
//    paint.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
//    // text shadow
//    paint.setShadowLayer(1f, 0f, 1f, Color.BLACK)
//
//    // draw text to the Canvas center
//    val bounds = Rect()
//    //draw the first text
//    paint.getTextBounds(text1, 0, text1.length, bounds)
//    var x = (bitmap.width - bounds.width()) / 2f - 470
//    var y = (bitmap.height + bounds.height()) / 2f - 140
//    canvas.drawText(text1, x, y, paint)
//
//    return bitmap
//}

fun drawTextToBitmap(text: String, textSize: Float, textColor: Int): Bitmap {
    val paint = Paint(ANTI_ALIAS_FLAG)
    paint.textSize = attr.textSize.toFloat()
    paint.color = attr.textColor
    paint.textAlign = Align.LEFT
    val baseline = -paint.ascent() // ascent() is negative

    val width = (paint.measureText(text) + 0.5f).toInt() // round

    val height = (baseline + paint.descent() + 0.5f).toInt()
    val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(image)
    canvas.drawText(text, 0f, baseline, paint)
    return image
}

internal object GeofencingConstants {
    const val EXTRA_GEOFENCE_INDEX = "GEOFENCE_INDEX"
    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.DAYS.toMillis(1)
}