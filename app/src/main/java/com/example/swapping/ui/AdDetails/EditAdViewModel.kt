package com.example.swapping.ui.AdDetails

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class EditAdViewModel {
    fun getImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }
}