package com.example.swapping.ui.userLogin

import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.google.android.material.textfield.TextInputLayout

class RegisterViewModel(val DBHelper: DataBaseHelper) : ViewModel() {
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

    fun validateForm(
        email: String?,
        password: String?,
        username: String?,
        birthDate: Boolean?,
        permission: Boolean?
    ): HashMap<String, Boolean>  {

        val isValidEmail =
            email != null && email.isNotBlank() && email.contains("@") && email.contains(".") && isEmailAvailable(email)
        val isValidPassword = password != null && password.isNotBlank() && password.length >= 6
        val isValidNickName = username != null && username.isNotBlank() && isUsernameAvailable(username)
        val isValidBirthDate = birthDate == true
        val isCheckedPermission = permission == true
        return hashMapOf(
            "BirthDate" to isValidBirthDate,
            "Permission" to isCheckedPermission,
            "Email" to isValidEmail,
            "Password" to isValidPassword,
            "Username" to isValidNickName
        )
    }

    private fun isUsernameAvailable(username: String) : Boolean {
        val usersList = DBHelper.getAllUsers()
        var contains = false
        for(data in usersList){
            if (data.first == username)
                contains = true
        }
        return !contains
    }

    private fun isEmailAvailable(email: String) : Boolean {
        val usersList = DBHelper.getAllUsers()
        var contains = false
        for(data in usersList){
            if (data.second == email)
                contains = true
        }
        return !contains
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