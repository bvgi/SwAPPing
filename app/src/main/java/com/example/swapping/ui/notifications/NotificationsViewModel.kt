package com.example.swapping.ui.notifications

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.Negotiation

class NotificationsViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun getNegotiations(userID: Int, context: Context): Pair<Array<Triple<Pair<Int, String>, Pair<Int, String>, Int>>, Array<Pair<Ad, Negotiation>>> {
        val dbHelper = DataBaseHelper(context)
        val userNegotiations = dbHelper.getUserNegotiations(userID)
        val ownedAds = mutableListOf<Triple<Pair<Int, String>, Pair<Int, String>, Int>>()
        for(pair in userNegotiations){
            val ad = pair.first
            val negotiation = pair.second
            val purchaser = dbHelper.getUserById(negotiation.purchaserID).username
            ownedAds.add(
                Triple(
                    Pair(ad.ID, ad.title),
                    Pair(negotiation.purchaserID, purchaser),
                    negotiation.type
                )
            )
        }
        return Pair(ownedAds.toTypedArray(), userNegotiations)
    }

}