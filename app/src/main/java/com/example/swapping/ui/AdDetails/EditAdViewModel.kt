package com.example.swapping.ui.AdDetails

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import java.io.ByteArrayOutputStream

class EditAdViewModel {
    fun getImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun getAd(ID: Int, context: Context) : Ad {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getAnnouncement(ID)
    }

    fun getVoivodeships(context: Context) : Array<String> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getVoivodeships()
    }

    fun getCategories(context: Context) : Array<String> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getCategories()
    }

    fun getStatuses(context: Context) : Array<String> {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getStatuses()
    }

    fun updateAd(ad: Ad, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.updateAnnouncement(ad)
    }

    fun getBytes(imageView: ImageView, image: Drawable?): ByteArray {
        val stream = ByteArrayOutputStream()
        if(image == null) {
            val bitmap = imageView.drawable.toBitmap(
                imageView.drawable.intrinsicWidth,
                imageView.drawable.intrinsicHeight
            )
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        } else {
            val bitmap =  imageView.drawable.toBitmap(
                image.intrinsicWidth,
                image.intrinsicHeight
            )
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        }
        return stream.toByteArray()
    }

}