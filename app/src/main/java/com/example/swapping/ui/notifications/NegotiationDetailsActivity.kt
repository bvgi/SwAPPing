package com.example.swapping.ui.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.profile.ProfileViewActivity
import com.example.swapping.ui.userAds.UserAdsActivity
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.ui.newAd.NewAdViewModel
import com.google.android.material.snackbar.Snackbar


class NegotiationDetailsActivity : AppCompatActivity() {
    private lateinit var negotiationType: TextView
    private lateinit var title: TextView
    private lateinit var username: TextView
    private lateinit var offers: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var accept: Button
    private lateinit var rise: Button
    private lateinit var reject: Button
    private val networkConnection = NetworkConnection()

    private var userID = 0
    private var negotiationID = 0

    private lateinit var negotiationDetailsViewModel: NegotiationDetailsViewModel

    private val arguments: NegotiationDetailsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negotiation_details)

        arguments.let {
            userID = it.userID
            negotiationID = it.negotiationID
        }

        negotiationDetailsViewModel = ViewModelProvider(this).get(NegotiationDetailsViewModel::class.java)

        negotiationType = findViewById(R.id.negotiationType)
        title = findViewById(R.id.negotiationAdTitle)
        username = findViewById(R.id.negotiationUsername)
        offers = findViewById(R.id.negotiationOffer)
        accept = findViewById(R.id.acceptNegotiation)
        rise = findViewById(R.id.riseNegotiation)
        reject = findViewById(R.id.rejectNegotitation)

        val negotiation = negotiationDetailsViewModel.getNegotiation(negotiationID, this)

        negotiationType.text = when(negotiation.type){
            1 -> "Nowa negocjacja"
            2 -> "Negocjacja zaakceptowana"
            3 -> "Negocjacja w trakcie"
            4 -> "Negocjacja odrzucona"
            else -> "Brak danych"
        }

        val ad = negotiationDetailsViewModel.getAd(negotiation.adID, this)
        val owner = negotiationDetailsViewModel.getUserById(negotiation.ownerID, this) // w??a??ciciel og??oszenia
        val user = negotiationDetailsViewModel.getUserById(negotiation.purchaserID, this) // ten kt??ry rozpocz???? negocjacj??

        if(negotiation.ownerID == userID)
            username.text = user.username
        else
            username.text = owner.username

        username.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, ProfileViewActivity::class.java)
                intent.putExtras(bundleOf("userID" to userID, "profileID" to owner.ID))
                startActivity(intent)
            }
        }

        title.text = ad.title

        title.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, AdDetailsActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "userID" to userID,
                        "profileID" to owner.ID,
                        "adID" to ad.ID
                    )
                )
                startActivity(intent)
            }
        }

        val offersID = negotiation.offers.removeSurrounding("[", "]").split(", ").map { it.toInt() }
        val offersAd = mutableListOf<Ad>()
        for(id in offersID){
            offersAd.add(negotiationDetailsViewModel.getAd(id, this))
        }

        adapter = HomeAdapter(offersAd.toTypedArray(), this)
        offers.layoutManager = GridLayoutManager(this, 3)
        offers.adapter = adapter
        adapter.notifyDataSetChanged()

        val context = this

        adapter.setOnClickListener(object : HomeAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                if (!networkConnection.isNetworkAvailable(applicationContext)) {
                    Snackbar.make(
                        findViewById(R.id.noInternet),
                        "Brak dost??pu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(context, AdDetailsActivity::class.java)
                    intent.putExtras(
                        bundleOf(
                            "userID" to userID,
                            "profileID" to owner.ID,
                            "adID" to offersAd[pos].ID
                        )
                    )
                    startActivity(intent)
                }
            }
        })

        if(user.ID == userID){
            accept.visibility = View.GONE
            if(negotiation.type != 3){
                rise.visibility = View.GONE
                reject.visibility = View.GONE
            }
        }
        if(ad.archived == 1 || negotiation.type == 4){
            accept.visibility = View.GONE
            reject.visibility = View.GONE
            rise.visibility = View.GONE
        }


        accept.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                negotiationDetailsViewModel.accept(negotiationID, this)
                negotiationDetailsViewModel.archive(ad.ID, user.ID, this)
                accept.isEnabled = false
                reject.isEnabled = false
                rise.isEnabled = false
                for(offer in offersAd){
                    negotiationDetailsViewModel.archive(offer.ID, userID, this)
                }
            }
        }

        reject.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                negotiationDetailsViewModel.reject(negotiationID, this)
            }
        }

        rise.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (user.ID == userID) {
                    val intent = Intent(this, UserAdsActivity::class.java)
                    intent.putExtras(
                        bundleOf(
                            "profileID" to owner.ID,
                            "userID" to userID,
                            "adID" to ad.ID,
                            "prev" to "Rise",
                            "negotiationID" to negotiationID
                        )
                    )
                    startActivity(intent)
                } else {
                    negotiationDetailsViewModel.rise(negotiationID, this)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

}