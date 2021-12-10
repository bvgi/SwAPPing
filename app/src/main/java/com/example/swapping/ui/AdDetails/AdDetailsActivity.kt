package com.example.swapping.ui.AdDetails

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavArgument
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.swapping.MainActivity
import com.example.swapping.R
import com.example.swapping.ui.home.HomeFragmentDirections
import com.example.swapping.ui.profile.ProfileViewFragment

class AdDetailsActivity : AppCompatActivity() {

    val args: AdDetailsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_details)

        val userID = args.userID
        val profileID = args.profileID
        val adID = args.adID



        println("AD ACTIVITY: $userID, $adID, $profileID")

        val navController = findNavController(R.id.nav_host_fragment_activity_ad_details)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_ad_details, R.id.navigation_ad_profile))

        navController.graph.findNode(R.id.navigation_ad_details)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())
        navController.graph.findNode(R.id.navigation_ad_details)
            ?.addArgument("profileID", NavArgument.Builder().setDefaultValue(profileID).build())
        navController.graph.findNode(R.id.navigation_ad_details)
            ?.addArgument("adID", NavArgument.Builder().setDefaultValue(adID).build())

        navController.graph.findNode(R.id.navigation_ad_profile)
            ?.addArgument("userID", NavArgument.Builder().setDefaultValue(userID).build())
        navController.graph.findNode(R.id.navigation_ad_profile)
            ?.addArgument("profileID", NavArgument.Builder().setDefaultValue(profileID).build())

        setupActionBarWithNavController(navController, appBarConfiguration)

    }
}