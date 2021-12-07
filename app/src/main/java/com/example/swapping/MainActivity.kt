
package com.example.swapping

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var DBHelper: DataBaseHelper
    lateinit var registerLink: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBHelper = DataBaseHelper(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras: Bundle
        extras = intent.extras!!
        val userID = extras.getInt("userid")

        val navView: BottomNavigationView = binding.navView
        if(savedInstanceState == null)
            navView.selectedItemId = R.id.navigation_home
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_new_announcement, R.id.navigation_notifications, R.id.navigation_profile))
        navController.graph.findNode(R.id.navigation_new_announcement)
            ?.addArgument("userid", NavArgument.Builder().setDefaultValue(userID).build())
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}