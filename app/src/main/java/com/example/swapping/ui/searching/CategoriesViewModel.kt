package com.example.swapping.ui.searching

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper

class CategoriesViewModel : ViewModel() {

    fun getCategories(context: Context) : Array<String> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getCategories()
    }
}