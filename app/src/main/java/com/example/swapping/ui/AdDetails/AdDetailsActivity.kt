package com.example.swapping.ui.AdDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.Models.User
import com.example.swapping.R
import com.example.swapping.ui.MainActivity
import com.example.swapping.ui.profile.ProfileViewActivity
import com.example.swapping.ui.userAds.UserAdsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class AdDetailsActivity : AppCompatActivity() {

    private lateinit var adDetailsViewModel: AdDetailsViewModel
    private lateinit var editAd: MenuItem
    private lateinit var deleteAd: MenuItem
    private lateinit var adTitle: TextView
    private lateinit var adPhoto: ImageView
    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var description: TextView
    private lateinit var location: TextView
    private lateinit var category: TextView
    private lateinit var status: TextView
    private lateinit var rateStars: Array<ImageView>
    private lateinit var dbHelper: DataBaseHelper
    private var purchaser = 0
    private var archived = 0
    private lateinit var goToUserArrow: ImageView
    private lateinit var goToUser: LinearLayout
    private lateinit var publishedDate: TextView
    private lateinit var startNegotiation: FloatingActionButton

    private val networkConnection = NetworkConnection()
    private lateinit var likeAd: MenuItem
    private lateinit var purchaseItem: MenuItem

    private var userID = 0
    private var adID = 0
    private var profileID = 0
    private var prev = ""

    private lateinit var user: User

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_details)

        adDetailsViewModel = AdDetailsViewModel()
        dbHelper = DataBaseHelper(this)

        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            profileID = extras.getInt("profileID")
            adID = extras.getInt("adID")
            prev = extras.getString("previous").toString()
        }

        val userNegotiations = adDetailsViewModel.getUserNegotiations(userID, this)
        user = adDetailsViewModel.getUserInfo(profileID, this)
        val ad = adDetailsViewModel.getAd(adID, this)
        val photo = adDetailsViewModel.getImage(ad.image)
        val ads = adDetailsViewModel.getUserAnnouncements(userID, this)

        archived = ad.archived
        purchaser = ad.purchaser_id

        adTitle = findViewById(R.id.Title)
        adTitle.text = ad.title
        title = ""

        adPhoto = findViewById(R.id.AdImage)
        adPhoto.setImageBitmap(photo)

        publishedDate = findViewById(R.id.publishedDate)
        publishedDate.text = "${ad.published_date.toString().substring(6,8)}" +
                ".${ad.published_date.toString().substring(4,6)}" +
                ".${ad.published_date.toString().substring(0,4)}"

        description = findViewById(R.id.adDescription)
        description.text = ad.description

        name = findViewById(R.id.adOwnerName)
        name.text = user.name

        username = findViewById(R.id.ownerUsername)
        username.text = "@" + user.username

        val star1 = findViewById<ImageView>(R.id.adStar1)
        val star2 = findViewById<ImageView>(R.id.adStar2)
        val star3 = findViewById<ImageView>(R.id.adStar3)
        val star4 = findViewById<ImageView>(R.id.adStar4)
        val star5 = findViewById<ImageView>(R.id.adStar5)

        rateStars = arrayOf(star1, star2, star3, star4, star5)
        adDetailsViewModel.changeStars(rateStars, user.mean_rate)

        goToUserArrow = findViewById(R.id.goToUserArrow)

        goToUser = findViewById(R.id.goToUser)
        goToUser.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, ProfileViewActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "userID" to userID,
                        "profileID" to profileID)
                )
                startActivity(intent)
            }
        }

        location = findViewById(R.id.locationName)
        if (ad.city != "-")
            location.text = ad.city + ", " + ad.voivodeship
        else
            location.text = ad.voivodeship

        category = findViewById(R.id.adCategory)
        category.text = ad.category

        status = findViewById(R.id.adStatus)
        status.text = ad.status

        startNegotiation = findViewById(R.id.startNegotiationButton)
        if(userID == profileID)
            startNegotiation.visibility = View.GONE
        else
            startNegotiation.visibility = View.VISIBLE

        var isInNegotiation = false
        for(pair in userNegotiations){
            if(pair.second.adID == adID && pair.second.purchaserID == userID)
                isInNegotiation = true
        }
        if(ads.isEmpty() || isInNegotiation)
            startNegotiation.isEnabled = false
        startNegotiation.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (ad.purchaser_id != userID) {
                    val intent = Intent(this, UserAdsActivity::class.java)
                    intent.putExtras(
                        bundleOf(
                            "profileID" to profileID,
                            "userID" to userID,
                            "adID" to adID
                        )
                    )
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(archived == 1){
            menuInflater.inflate(R.menu.ad_details_archived, menu)
            purchaseItem = menu!!.findItem(R.id.menu_purchased)
            if(userID != profileID)
                purchaseItem.setIcon(R.drawable.bought_icon)
            else
                purchaseItem.setIcon(R.drawable.ic_baseline_compare_arrows_24)
        } else {
            if(userID != profileID){
                menuInflater.inflate(R.menu.menu_home_ad_details, menu)
                likeAd = menu!!.findItem(R.id.menu_likeAd)
                if (adDetailsViewModel.isLiked(userID, adID, this))
                    likeAd.setIcon(R.drawable.ic_baseline_favorite_24)
            } else {
                menuInflater.inflate(R.menu.menu_ad_details, menu)
                if (menu != null) {
                    editAd = menu.findItem(R.id.menu_editAd)
                    deleteAd = menu.findItem(R.id.menu_deleteAd)
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_editAd -> {
                val intent = Intent(this, EditAdActivity::class.java)
                intent.putExtra("adID", adID)
                startActivity(intent)
                return true
            }
            R.id.menu_deleteAd -> {
                adDetailsViewModel.deleteAd(adID, this)
                adDetailsViewModel.deleteNegotiation(adID, this)
                onBackPressed()
                return true
            }
            R.id.menu_likeAd -> {
                adDetailsViewModel.addToLiked(userID, adID, likeAd, this)
                return true
            }
            android.R.id.home ->{
                if(prev != "Edit")
                    onBackPressed()
                else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtras(bundleOf("userID" to userID, "adID" to adID))
                    startActivity(intent)
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}