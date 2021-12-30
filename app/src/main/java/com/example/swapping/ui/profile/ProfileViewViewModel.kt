package com.example.swapping.ui.profile

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Review
import com.example.swapping.Models.User
import com.example.swapping.R
import kotlin.math.round

class ProfileViewViewModel : ViewModel() {

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
        var contains = false
        for(user in followers){
            contains = user.first == userID
        }
        return contains
    }

    fun addReview(review: Review, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.addReview(review)
    }

    fun deleteReview(profileID: Int, userID: Int, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.deleteReview(profileID, userID)
    }

    fun updateMeanRate(profileID: Int, meanRate: Double, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.updateMeanRate(
            profileID,
            meanRate
        )
    }

    fun getUser(ID: Int, context: Context) : User {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserById(ID)
    }

    fun getFollowersSize(ID: Int, context: Context) : Int {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getFollowers(ID).size
    }

    fun getFollowingSize(ID: Int, context: Context) : Int {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getFollowing(ID).size
    }

    fun follow(profile: Int, user: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.addFollower(profile, user)
    }

    fun unfollow(profile: Int, user: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.deleteFollower(profile, user)
    }
}