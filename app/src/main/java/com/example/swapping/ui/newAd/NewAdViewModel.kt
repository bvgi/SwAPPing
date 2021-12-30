package com.example.swapping.ui.newAd

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream

class NewAdViewModel : ViewModel() {

    fun registerErrors(valid: HashMap<String, Boolean>, titleLayout: TextInputLayout, descriptionLayout: TextInputLayout, voivodeshipLayout: TextInputLayout, categoryLayout: TextInputLayout, statusLayout: TextInputLayout) : Boolean {
        for ((key, value) in valid.entries) {
            when (key) {
                "Title" -> {
                    if (!value) titleLayout.error = "Podaj tytuł"
                    else titleLayout.error = null
                }
                "Description" -> {
                    if (!value) descriptionLayout.error = "Wypełnij opis"
                    else descriptionLayout.error = null
                }
                "Voivodeship" -> {
                    if (!value) voivodeshipLayout.error =
                        "Wybierz województwo"
                    else voivodeshipLayout.error = null
                }
                "Category" -> {
                    if (!value) categoryLayout.error = "Wybierz kategorię"
                    else categoryLayout.error = null
                }
                "Status" -> {
                    if (!value) statusLayout.error =
                        "Wybierz stan zużycia"
                    else statusLayout.error = null
                }
            }
        }
        return !valid.containsValue(false)
    }

    fun validateForm(
        title: String?,
        description: String?,
        voivodeship: String?,
        category: String?,
        status: String?
    ): HashMap<String, Boolean>  {
        val isValidTitle = title != null && title.isNotBlank()
        val isValidDescription = description != null && description.isNotBlank() && description.length <= 255
        val isValidVoivodeship = voivodeship != null && voivodeship.isNotBlank()
        val isValidCategory = category != null && category.isNotBlank()
        val isValidStatus = status != null && status.isNotBlank()
        return hashMapOf(
            "Title" to isValidTitle,
            "Description" to isValidDescription,
            "Voivodeship" to isValidVoivodeship,
            "Category" to isValidCategory,
            "Status" to isValidStatus
        )
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

    fun addAd(ad: Ad, context: Context) {
        val dbHelper = DataBaseHelper(context)
        dbHelper.addAnnouncement(ad)
    }

}