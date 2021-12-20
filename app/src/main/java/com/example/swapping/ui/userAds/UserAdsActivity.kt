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
    var profileID = 0
    var prev = ""
    var negotiationID = 0
    var type = 0
    private var chosedAds = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_ads)
        arguments.let {
            userID = it.userID
            adID = it.adID
            profileID = it.profileID
        }

        val extras: Bundle? = intent.extras
        if(extras != null){
            prev = extras.getString("prev").toString()
            negotiationID = extras.getInt("negotiationID")
        }

        dbHelper = DataBaseHelper(this)

        val negotiation = dbHelper.getNegotiation(negotiationID)
        type = negotiation.type

        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel()::class.java)

        val adsList = homeViewModel.getUserAnnouncements(userID, this).toMutableList()

        for(ad in adsList){
            if(ad.archived == 1){
                adsList.drop(adsList.indexOf(ad))
            }
        } //TODO: Usunąć ogłoszenia które biorą już udział w negocjacjach

        ads = adsList.toTypedArray()

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
                    chosedAds.add(ads[pos].ID)
                else
                    chosedAds.remove(ads[pos].ID)
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
                if(type == 3){
                    dbHelper.updateNegotiation(negotiationID, chosedAds.toString())
                    onBackPressed()
                } else {
                    dbHelper.startNegotiation(
                        adID,
                        profileID,
                        userID,
                        chosedAds.toString()
                    ) // TODO: zapisywanie, co zostało zaoferowane
                    onBackPressed()
                    Handler(Looper.getMainLooper()).postDelayed({
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Negocjacja rozpoczęta",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }, 5000)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtra("userID", userID)
    }
}