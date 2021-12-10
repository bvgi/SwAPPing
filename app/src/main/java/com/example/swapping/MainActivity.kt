
package com.example.swapping

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var DBHelper: DataBaseHelper
    lateinit var registerLink: TextView
    lateinit var navView: BottomNavigationView
    var userID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBHelper = DataBaseHelper(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras: Bundle? = intent.extras
        if(extras != null)
            userID = extras.getInt("userID")

        println("MAIN::$userID")



        navView = binding.navView

        if(savedInstanceState == null)
            navView.selectedItemId = R.id.navigation_home

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_new_announcement, R.id.navigation_notifications, R.id.navigation_profile, R.id.navigation_profile_view))

        navController.setGraph(R.navigation.mobile_navigation, extras)



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            navView.selectedItemId = R.id.navigation_profile
        }
    }
}