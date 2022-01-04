package com.example.swapping.ui.userAds

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.Negotiation

class UserAdsViewModel : ViewModel(){

    fun getNotArchivedUserAds(userID: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getUserAnnouncementsNotArchived(userID)

        return ads
    }

    fun getNegotiation(negotiationID: Int, context: Context): Negotiation {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getNegotiation(negotiationID)
    }

    fun updateNegotiation(id: Int, text: String, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.updateNegotiation(id, text)
    }

    fun startNegotiation(adID: Int, profileID: Int, userID: Int, offers: String, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.startNegotiation(
            adID,
            profileID,
            userID,
            offers
        )
    }

    fun getUserLiked(userID: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getLiked(userID)

        return ads
    }

    fun getPurchasedAds(userID: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getPurchasedAnnouncements(userID)


        return ads
    }

    fun getUserAds(userID: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getUserAnnouncements(userID)
        return ads
    }
}
