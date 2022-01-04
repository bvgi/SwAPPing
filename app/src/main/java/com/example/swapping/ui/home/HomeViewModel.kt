package com.example.swapping.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getFollowersAnnouncements(userID: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getFollowingAds(userID)

        return ads
    }

    fun getNotArchivedAds(userID: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getNotArchivedAds(userID)

        return ads
    }

}