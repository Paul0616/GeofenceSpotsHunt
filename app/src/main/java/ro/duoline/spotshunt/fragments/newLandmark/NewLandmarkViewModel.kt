package ro.duoline.spotshunt.fragments.newLandmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ro.duoline.spotshunt.database.LandmarkDatabaseDao

class NewLandmarkViewModel(app: Application, val database: LandmarkDatabaseDao) : AndroidViewModel(app) {

}
