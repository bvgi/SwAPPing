package com.example.swapping.ui.searching

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad

class ClueWordSearchViewModel : ViewModel() {
    fun findAds(text: String, ID: Int, context: Context): Array<Ad> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.findAds(text, ID)
    }
}