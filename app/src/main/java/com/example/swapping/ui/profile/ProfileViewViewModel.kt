package com.example.swapping.ui.profile

import android.content.Context
import android.provider.ContactsContract
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Review
import com.example.swapping.R

class ProfileViewViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text

    fun changeStars(starsArray: Array<ImageView>, starNumber: Int){
        for(i in 0 until starNumber) {
            starsArray[i].setImageResource(R.drawable.ic_round_star_rate_24)
        }
        if(starNumber != 5)
            for(i in starNumber..4) {
                starsArray[i].setImageResource(R.drawable.ic_round_star_outline_24)
            }
    }

    fun getMeanRate(reviews: Array<Review>) : Double {
        val rates = mutableListOf<Int>()
        var meanRate = 0.0
        for(review in reviews)
            rates.add(review.rate)
        if (rates.size != 0)
            meanRate = rates.average()
        return meanRate
    }

    fun isReviewer(userID: Int, profileID: Int, context: Context) : Boolean {
        val dbHelper = DataBaseHelper(context)
        val reviewsArray = dbHelper.getUserReviews(profileID)
        var reviewer = false

        for(review in reviewsArray)
            if (review.reviewer == userID)
                reviewer = true

        return reviewer
    }

    fun getReviews(userID: Int, context: Context): Array<Review> {
        val dbHelper = DataBaseHelper(context)

        return dbHelper.getUserReviews(userID)
    }

    fun isFollower(userID: Int, profileID: Int, context: Context) : Boolean {
        val dbHelper = DataBaseHelper(context)
        val followers = dbHelper.getFollowers(profileID)
        val username = dbHelper.getUserById(userID).username
        return followers.contains(username)
    }
}