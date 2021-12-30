package com.example.swapping.ui.AdDetails

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.MenuItem
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.Negotiation
import com.example.swapping.Models.User
import com.example.swapping.R
import kotlin.math.floor

class AdDetailsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getAd(adID: Int, context: Context): Ad {
        val dbHelper = DataBaseHelper(context)
        val ad = dbHelper.getAnnouncement(adID)
        return ad
    }

    fun getImage(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun getUserInfo(userID: Int, context: Context) : User {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserById(userID)
    }

    fun changeStars(starsArray: Array<ImageView>, starNumber: Double){
        val value = floor(starNumber).toInt()
        for(i in 0 until value) {
            starsArray[i].setImageResource(R.drawable.ic_round_star_rate_24)
        }
        if((starNumber - value.toDouble()) != 0.0){
            starsArray[value].setImageResource(R.drawable.ic_round_star_half_24)
        }
    }

    fun getUserNegotiations(ID: Int, context: Context) : Array<Pair<Ad, Negotiation>> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserNegotiations(ID)
    }

    fun getUserAnnouncements(ID: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserAnnouncements(ID)
    }

    fun addToLiked(userID: Int, adID: Int, menuItem: MenuItem, context: Context){
        val dbHelper = DataBaseHelper(context)
        if (isLiked(userID, adID, context)) {
            dbHelper.deleteLiked(userID, adID)
            menuItem.setIcon(R.drawable.ic_twotone_favorite_border_24)
        } else {
            dbHelper.addLiked(userID, adID)
            menuItem.setIcon(R.drawable.ic_baseline_favorite_24)
        }
    }

    fun isLiked(userID: Int, adID: Int, context: Context) : Boolean {
        val dbHelper = DataBaseHelper(context)
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

    fun deleteAd(ID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.deleteAd(ID)
    }

    fun deleteNegotiation(ID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.deleteNegotiation(ID)
    }
}