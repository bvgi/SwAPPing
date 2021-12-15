package com.example.swapping.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getUserAnnouncements(userID: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getUserAnnouncements(userID)
        return ads
    }

    fun getAnnouncements(userID: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getAnnouncements(userID)

        return ads
    }

    fun getUserLiked(userID: Int, context: Context) : Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        val ads = dbHelper.getLiked(userID)

        return ads
    }
}