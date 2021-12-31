package com.example.swapping.ui.userLogin

import android.content.Context
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.User
import com.google.android.material.textfield.TextInputLayout

class RegisterViewModel() : ViewModel() {
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
        permission: Boolean?,
        context: Context
    ): HashMap<String, Boolean>  {

        val isValidEmail =
            email != null && email.isNotBlank() && email.contains("@") && email.contains(".") && isEmailAvailable(email, context)
        val isValidPassword = password != null && password.isNotBlank() && password.length >= 6
        val isValidNickName = username != null && username.isNotBlank() && isUsernameAvailable(username, context)
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

    private fun isUsernameAvailable(username: String, context: Context) : Boolean {
        val dbHelper = DataBaseHelper(context)
        val usersList = dbHelper.getAllUsers()
        var contains = false
        for(data in usersList){
            if (data.first == username)
                contains = true
        }
        return !contains
    }

    private fun isEmailAvailable(email: String, context: Context) : Boolean {
        val dbHelper = DataBaseHelper(context)
        val usersList = dbHelper.getAllUsers()
        var contains = false
        for(data in usersList){
            if (data.second == email)
                contains = true
        }
        return !contains
    }

    fun addUser(user: User, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.addUser(user)
    }
}