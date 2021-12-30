package com.example.swapping.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper

class ProfileViewModel : ViewModel() {
    fun logOut(ID: Int, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.setLoggedOut(ID)
    }
}