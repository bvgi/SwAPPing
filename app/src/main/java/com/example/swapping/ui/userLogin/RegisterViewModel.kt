package com.example.swapping.ui.userLogin

import android.content.Intent
import android.text.Layout
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.MainActivity
import com.google.android.material.textfield.TextInputLayout

class RegisterViewModel : ViewModel() {
    fun registerErrors(isValid: HashMap<String, Boolean>, birthCheckBox: CheckBox, permissionCheck: CheckBox, emailLayout: TextInputLayout, passwordLayout: TextInputLayout, usernameLayout: TextInputLayout) : Boolean {
        for((key,value) in isValid.entries) {
            when (key) {
                "BirthDate" -> {
                    if (!value) birthCheckBox.error = "Obowiązkowe pole"
                    else birthCheckBox.error = null
                }
                "Permission" -> {
                    if (!value) permissionCheck.error = "Obowiązkowe pole"
                    else permissionCheck.error = null
                }
                "Email" -> {
                    if (!value) emailLayout.error = "Adres e-mail już istnieje lub jest niepoprawny"
                    else emailLayout.error = null
                }
                "Password" -> {
                    if (!value) passwordLayout.error = "Niepoprawne hasło"
                    else passwordLayout.error = null
                }
                "Username" -> {
                    if (!value) usernameLayout.error = "Nazwa użytkownika już istnieje lub jest niepoprawna"
                    else usernameLayout.error = null
                }
            }
        }
        return !isValid.containsValue(false)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text



//    private fun makeRequest() {
//        val queue = SingletonManager.queue
//
//        val url = "http://192.168.0.45:8000/Users"
//
//        val django = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            { response ->
//                Log.d("Response", response.toString())
//            },
//            { response ->
//                Log.d("Error", response.toString())
//            })
//
//        queue.add(django)
//    }
}