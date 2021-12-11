package com.example.swapping.ui.AdDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.Models.User
import com.example.swapping.R

class AdDetailsActivity : AppCompatActivity() {

    private lateinit var adDetailsViewModel: AdDetailsViewModel
    private lateinit var editAd: MenuItem
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

    var userID = 0
    var adID = 0
    var profileID = 0

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
        }

        println("AD FRAGMENT: $userID, $adID, $profileID")

        val user = adDetailsViewModel.getUserInfo(profileID, this)
        val ad = adDetailsViewModel.getAd(adID, this)
        val photo = adDetailsViewModel.getImage(ad.image)

        title = findViewById(R.id.Title)
        title.text = ad.title

        adPhoto = findViewById(R.id.AdImage)
        adPhoto.setImageBitmap(photo) // TODO: Poprawić rozdzielczość

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ad_details, menu)
        if (menu != null)
            editAd = menu.findItem(R.id.menu_editAd)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_editAd)
            editAd = item

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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtras( bundleOf("userID" to userID, "adID" to adID))
    }
}