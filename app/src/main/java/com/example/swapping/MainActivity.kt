
package com.example.swapping

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    // TODO: sprawdzanie internetu

    private lateinit var binding: ActivityMainBinding
    lateinit var DBHelper: DataBaseHelper
    lateinit var registerLink: TextView
    lateinit var navView: BottomNavigationView
    var userID = 0
    var adID = 0
    var exitCounter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBHelper = DataBaseHelper(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            adID = extras.getInt("adID")
        }


        println("MAIN::$userID $adID")


        navView = binding.navView

        if(savedInstanceState == null)
            navView.selectedItemId = R.id.navigation_home

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_new_announcement, R.id.navigation_notifications, R.id.navigation_profile, R.id.navigation_profile_view))

        navController.setGraph(R.navigation.mobile_navigation, extras)


        if(adID != 0){ // TODO: Powrót z AdDetailsActivity
//            val profileFragment = ProfileFragment()
//            profileFragment.arguments = bundleOf("userID" to userID, "previousFragment" to "AdDetails")
//            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, profileFragment).commit()
            if(adID == -1)
                navController.navigate(R.id.navigation_search, bundleOf("userID" to userID, "previousFragment" to "AdDetails"))
            else
                navController.navigate(R.id.navigation_profile, bundleOf("userID" to userID, "previousFragment" to "AdDetails"))
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
            finishAffinity()
            exitCounter = 0
//
////            super.onBackPressed()
////            val intent = Intent(Intent.ACTION_MAIN)
////            intent.addCategory(Intent.CATEGORY_HOME)
////            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
////            startActivity(intent)
        }
    }
}