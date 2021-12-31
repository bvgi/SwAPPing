
package com.example.swapping.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    lateinit var navView: BottomNavigationView
    private var userID = 0
    private var adID = 0
    private var exitCounter = 0
    private val networkConnection = NetworkConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!networkConnection.isNetworkAvailable(applicationContext)) {
            Snackbar.make(
                findViewById(R.id.noInternet),
                "Brak dostępu do internetu",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            adID = extras.getInt("adID")
        }

        navView = binding.navView

        if(savedInstanceState == null)
            navView.selectedItemId = R.id.navigation_home

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_search,
            R.id.navigation_new_announcement,
            R.id.navigation_notifications,
            R.id.navigation_profile
        ))

        navController.setGraph(R.navigation.mobile_navigation, extras)


        if(adID != 0){
            if(adID == -1)
                navController.navigate(R.id.navigation_search, bundleOf("userID" to userID, "previousFragment" to "adDetails"))
            else
                navController.navigate(R.id.navigation_profile, bundleOf("userID" to userID, "previousFragment" to "adDetails"))
        }

        navController.graph.findNode(R.id.navigation_new_announcement)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())
        navController.graph.findNode(R.id.navigation_profile)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())
        navController.graph.findNode(R.id.navigation_home)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())
        navController.graph.findNode(R.id.navigation_search)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())
        navController.graph.findNode(R.id.navigation_notifications)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

    }

    override fun onBackPressed() {
        if(exitCounter == 0){
            Snackbar.make(
                findViewById(R.id.exitInformation),
                "Naciśnij ponownie, aby wyjść z aplikacji",
                Snackbar.LENGTH_SHORT
            ).show()
            exitCounter += 1
            val handler = Handler()
            handler.postDelayed({
                                exitCounter = 0
            }, 5000)
        } else {
            mainViewModel.logOut(userID, this)
            finishAffinity()
            exitCounter = 0
        }
    }
}