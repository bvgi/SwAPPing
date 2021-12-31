package com.example.swapping.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper

class MainViewModel : ViewModel() {
    fun logOut(userID: Int, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.setLoggedOut(userID)
    }
}