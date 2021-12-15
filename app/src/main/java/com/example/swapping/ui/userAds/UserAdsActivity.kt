package com.example.swapping.ui.userAds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.Models.Ad
import com.example.swapping.Models.User
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.home.HomeFragmentArgs
import com.example.swapping.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class UserAdsActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var adapter: UserAdsAdapter
    lateinit var adsRecycler: RecyclerView
    lateinit var ads: Array<Ad>
    lateinit var dbHelper: DataBaseHelper

    val arguments: UserAdsActivityArgs by navArgs()
    var userID = 0
    var adID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_ads)
        arguments.let {
            userID = it.userID
            adID = it.adID
        }

        dbHelper = DataBaseHelper(this)

        var chosedAds = mutableListOf<Ad>()

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel()::class.java)

        ads = homeViewModel.getUserAnnouncements(userID, this)

        for(ad in ads){
            if(ad.archived == 1){
                ads.drop(ads.indexOf(ad))
            }
        }

        adapter = UserAdsAdapter(arrayOf(), this)
        println("USERADS::: Num of ads: ${ads.size}, userID: $userID")
        adsRecycler = findViewById(R.id.usersAds)
        adsRecycler.layoutManager = GridLayoutManager(this, 3)
        adsRecycler.adapter = adapter
        adapter.dataset = ads
        adapter.notifyDataSetChanged()

        adapter.setOnClickListener(object : UserAdsAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {
                val checkbox: CheckBox = aView as CheckBox
                if(checkbox.isChecked)
                    chosedAds.add(ads[pos])
                else
                    chosedAds.remove(ads[pos])
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_saveData -> {

                dbHelper.startNegotiation(adID, userID) // TODO: zapisywanie, co zostało zaoferowane
                onBackPressed()

                Handler(Looper.getMainLooper()).postDelayed({
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Negocjacja rozpoczęta",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }, 5000)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtra("userID", userID)
    }
}