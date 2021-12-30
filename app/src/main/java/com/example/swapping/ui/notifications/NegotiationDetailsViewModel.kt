package com.example.swapping.ui.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.Negotiation
import com.example.swapping.Models.User

class NegotiationDetailsViewModel : ViewModel() {

    fun getNegotiation(ID: Int, context: Context) : Negotiation {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getNegotiation(ID)
    }

    fun getUserById(ID: Int, context: Context) : User {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserById(ID)
    }

    fun getAd(ID: Int, context: Context) : Ad {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getAnnouncement(ID)
    }

    fun accept(ID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.acceptNegotiation(ID)
    }

    fun reject(ID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.rejectedNegotiation(ID)
    }

    fun rise(ID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.riseNegotiation(ID)
    }

    fun archive(adID: Int, userID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.archiveAd(adID, userID)
    }
}