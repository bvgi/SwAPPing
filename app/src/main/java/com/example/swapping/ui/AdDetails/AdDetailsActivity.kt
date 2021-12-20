package com.example.swapping.ui.AdDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.Models.User
import com.example.swapping.R
import com.example.swapping.ui.profile.ProfileViewActivity
import com.example.swapping.ui.profile.ProfileViewFragment

class AdDetailsActivity : AppCompatActivity() {

    private lateinit var adDetailsViewModel: AdDetailsViewModel
    private lateinit var editAd: MenuItem
    private lateinit var deleteAd: MenuItem
    private lateinit var purchased: MenuItem
    private lateinit var title: TextView
    private lateinit var adPhoto: ImageView
    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var description: TextView
    private lateinit var location: TextView
    private lateinit var category: TextView
    private lateinit var status: TextView
    private lateinit var rateStars: Array<ImageView>
    private lateinit var goToProfile: ImageView
    private lateinit var dbHelper: DataBaseHelper
    private var purchaser = 0
    private var archived = 0
    private lateinit var goToUserArrow: ImageView
    private lateinit var goToUser: LinearLayout

    private lateinit var likeAd: MenuItem

    var userID = 0
    var adID = 0
    var profileID = 0
    var prev = ""

    // TODO(Dodać przycisk rozpoczynania negocjacji)
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

        println("AD FRAGMENT: $userID, $adID, $profileID")

        val user = adDetailsViewModel.getUserInfo(profileID, this)
        val ad = adDetailsViewModel.getAd(adID, this)
        val photo = adDetailsViewModel.getImage(ad.image)

        archived = ad.archived
        purchaser = ad.purchaser_id

        title = findViewById(R.id.Title)
        title.text = ad.title

        adPhoto = findViewById(R.id.AdImage)
        adPhoto.setImageBitmap(photo)

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
        println("AD::::${user.ID}, RATE: ${user.mean_rate}")

        goToUserArrow = findViewById(R.id.goToUserArrow)

        if(userID != profileID)
            goToUserArrow.visibility = View.VISIBLE

        goToUser = findViewById(R.id.goToUser)
        goToUser.setOnClickListener {
            val intent = Intent(this, ProfileViewActivity::class.java)
            intent.putExtras(bundleOf("userID" to userID, "profileID" to profileID))
            startActivity(intent)
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
    }

    private fun addToLiked(){
        println("LIKED::: AdID: $adID, UserID: $userID")
        if (isLiked()) {
            dbHelper.deleteLiked(userID, adID)
            likeAd.setIcon(R.drawable.ic_twotone_favorite_border_24)
        } else {
            dbHelper.addLiked(userID, adID)
            likeAd.setIcon(R.drawable.ic_baseline_favorite_24)
        }
    }

    private fun isLiked() : Boolean {
        val likedAds = dbHelper.getLiked(userID)
        var exists = false
        if(likedAds.isEmpty()){
            exists = false
        } else {
            for (ad in likedAds) {
                if (ad.ID == adID)
                    exists = true
            }
        }
        return exists
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(archived == 1){
            menuInflater.inflate(R.menu.ad_details_archived, menu)
        } else {
            if(userID != profileID){
                menuInflater.inflate(R.menu.menu_home_ad_details, menu)
                likeAd = menu!!.findItem(R.id.menu_likeAd)
                if (isLiked())
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
                dbHelper.deleteAnnouncement(adID)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtras( bundleOf("userID" to userID, "adID" to adID))
                startActivity(intent)
                return true
            }
            R.id.menu_likeAd -> {
                addToLiked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getParentActivityIntent(): Intent? {
        return if(userID == profileID)
            super.getParentActivityIntent()?.putExtras( bundleOf("userID" to userID, "adID" to adID))
        else
            super.getParentActivityIntent()?.putExtras( bundleOf("userID" to userID, "adID" to -1))
    }


}