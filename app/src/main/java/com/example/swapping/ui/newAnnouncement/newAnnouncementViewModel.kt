package com.example.swapping.ui.newAnnouncement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewAnnouncementViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is new Announcement Fragment"
    }
    val text: LiveData<String> = _text
}