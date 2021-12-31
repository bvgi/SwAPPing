package com.example.swapping.ui.userAds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.example.swapping.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar

class UserAdsActivity : AppCompatActivity() {

    private lateinit var userAdsViewModel: UserAdsViewModel
    lateinit var adapter: UserAdsAdapter
    lateinit var adsRecycler: RecyclerView
    lateinit var ads: Array<Ad>

    private val arguments: UserAdsActivityArgs by navArgs()
    private var userID = 0
    private var adID = 0
    private var profileID = 0
    private var prev = ""
    private var negotiationID = 0
    private var type = 0
    private var chosedAds = mutableListOf<Int>()

    private val networkConnection = NetworkConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_ads)
        arguments.let {
            userID = it.userID
            adID = it.adID
            profileID = it.profileID
        }

        val extras: Bundle? = intent.extras
        if(extras != null) {
            prev = extras.getString("prev").toString()
            negotiationID = extras.getInt("negotiationID")
        }

        val negotiation = userAdsViewModel.getNegotiation(negotiationID, this)
        type = negotiation.type

        userAdsViewModel =
            ViewModelProvider(this).get(UserAdsViewModel()::class.java)

        ads = userAdsViewModel.getNotArchivedUserAds(userID, this)

        adapter = UserAdsAdapter(arrayOf(), this)
        adsRecycler = findViewById(R.id.usersAds)
        adsRecycler.layoutManager = GridLayoutManager(this, 3)
        adsRecycler.adapter = adapter
        adapter.dataset = ads
        adapter.notifyDataSetChanged()

        adapter.setOnClickListener(object : UserAdsAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {
                if (!networkConnection.isNetworkAvailable(applicationContext)) {
                    Snackbar.make(
                        findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val checkbox: CheckBox = aView as CheckBox
                    if (checkbox.isChecked)
                        chosedAds.add(ads[pos].ID)
                    else
                        chosedAds.remove(ads[pos].ID)
                }
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
                    userAdsViewModel.updateNegotiation(
                        negotiationID,
                        chosedAds.toString(),
                        this)
                    onBackPressed()
                } else {
                    userAdsViewModel.startNegotiation(
                        adID,
                        profileID,
                        userID,
                        chosedAds.toString(),
                        this
                    )
                    onBackPressed()
                }
                return true
            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtra("userID", userID)
    }
}