package com.example.swapping.ui.newAnnouncement

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream

class NewAnnouncementViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is new Announcement Fragment"
    }
    val text: LiveData<String> = _text

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

    fun cameraRequest(REQUEST_CAMERA: Int, context: Context, activity: Activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA
            )
        }
    }



}