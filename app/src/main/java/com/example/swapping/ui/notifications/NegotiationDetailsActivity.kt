package com.example.swapping.ui.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.AdDetails.AdDetailsFragmentDirections
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.profile.ProfileViewActivity
import com.example.swapping.ui.userAds.UserAdsActivity

class NegotiationDetailsActivity : AppCompatActivity() {
    private lateinit var negotiationType: TextView
    private lateinit var title: TextView
    private lateinit var username: TextView
    private lateinit var offers: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var accept: Button
    private lateinit var rise: Button
    private lateinit var reject: Button

    private var userID = 0
    private var negotiationID = 0

    private lateinit var dbHelper: DataBaseHelper

    private val arguments: NegotiationDetailsActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negotiation_details)

        arguments.let {
            userID = it.userID
            negotiationID = it.negotiationID
        }

        dbHelper = DataBaseHelper(this)

        negotiationType = findViewById(R.id.negotiationType)
        title = findViewById(R.id.negotiationAdTitle)
        username = findViewById(R.id.negotiationUsername)
        offers = findViewById(R.id.negotiationOffer)
        accept = findViewById(R.id.acceptNegotiation)
        rise = findViewById(R.id.riseNegotiation)
        reject = findViewById(R.id.rejectNegotitation)

        val negotiation = dbHelper.getNegotiation(negotiationID)

        negotiationType.text = when(negotiation.type){
            1 -> "Nowa negocjacja"
            2 -> "Negocjacja zaakceptowana"
            3 -> "Negocjacja w trakcie"
            4 -> "Negocjacja odrzucona"
            else -> "Brak danych"
        }

        val ad = dbHelper.getAnnouncement(negotiation.adID)
        val owner = dbHelper.getUserById(negotiation.ownerID)
        val user = dbHelper.getUserById(negotiation.purchaserID)

        if(negotiation.ownerID == userID)
            username.text = user.username
        else
            username.text = owner.username

        username.setOnClickListener {
            val intent = Intent(this, ProfileViewActivity::class.java)
            intent.putExtras(bundleOf("userID" to userID, "profileID" to owner.ID))
            startActivity(intent)
        }

        title.text = ad.title

        title.setOnClickListener {
            val intent = Intent(this, AdDetailsActivity::class.java)
            intent.putExtras( bundleOf("userID" to userID, "profileID" to owner.ID, "adID" to ad.ID))
            startActivity(intent)
        }

        val offersID = negotiation.offers.removeSurrounding("[", "]").split(", ").map { it.toInt() }
        val offersAd = mutableListOf<Ad>()
        for(id in offersID){
            offersAd.add(dbHelper.getAnnouncement(id))
        }
        println(offersAd)
        adapter = HomeAdapter(offersAd.toTypedArray(), this)
        offers.layoutManager = GridLayoutManager(this, 3)
        offers.adapter = adapter
        adapter.notifyDataSetChanged()

        if(user.ID == userID){
            accept.visibility = View.GONE
            reject.visibility = View.GONE
        }
        if(ad.archived == 1 || negotiation.type == 4){
            accept.visibility = View.GONE
            reject.visibility = View.GONE
            rise.visibility = View.GONE
        }

        accept.setOnClickListener {
            dbHelper.acceptNegotiation(ad.ID, userID, owner.ID)
            dbHelper.archiveAd(ad.ID, userID)
        }

        reject.setOnClickListener {
            dbHelper.rejectedNegotiation(negotiationID)
        }

        rise.setOnClickListener {
            if(user.ID == userID) {
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
                dbHelper.riseNegotiation(negotiationID)
            }
        }


    }
}