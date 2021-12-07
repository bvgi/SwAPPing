package com.example.swapping.ui.newAnnouncement

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout

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

}