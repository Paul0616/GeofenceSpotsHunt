package ro.duoline.spotshunt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import ro.duoline.spotshunt.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(binding.bottomNavigation, findNavController(R.id.nav_host))

//        binding.bottomNavigation.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home -> {
//                    Snackbar.make(binding.fragmentContainer, "1", Snackbar.LENGTH_SHORT).show()
//                    true
//                }
//                R.id.logIn -> {
//                    Snackbar.make(binding.main, "2", Snackbar.LENGTH_SHORT).show()
//                    true
//                }
//                else-> false
//            }
//        }
    }

}
