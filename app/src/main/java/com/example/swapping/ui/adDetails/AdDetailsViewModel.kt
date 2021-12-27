package com.example.swapping.ui.adDetails

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
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
}