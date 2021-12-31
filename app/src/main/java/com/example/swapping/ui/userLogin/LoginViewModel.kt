package com.example.swapping.ui.userLogin

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.User
import com.google.android.material.textfield.TextInputLayout

class LoginViewModel : ViewModel() {

    fun loginErrors(isValid: Int, username: TextInputLayout, passwordLayout: TextInputLayout){
        when (isValid) {
            2 -> {
                passwordLayout.error = null
                username.error = "Niepoprawna nazwa"
            }
            3 -> {
                username.error = null
                passwordLayout.error = "Niepoprawne hasło"
            }
            1 -> {
                username.error = "Niepoprawna nazwa"
                passwordLayout.error = "Niepoprawne hasło"
            }
            else -> {
                username.error = null
                passwordLayout.error = null

            }
        }
    }

    fun validateForm(username: String?, password: String?, context: Context): Int {
        val dbHelper = DataBaseHelper(context)
        val isValidUsername = username != null && username.isNotBlank()
                && dbHelper.getUserByUsername(username).ID > -1
        val isValidPassword = password != null && password.isNotBlank()
                && password.length >= 6 && dbHelper.getUserByUsername(username!!).password == password
        return if (isValidUsername && isValidPassword) 0
        else if (!isValidPassword && !isValidUsername) 1
        else if (!isValidUsername) 2
        else 3
    }

    fun getUser(username: String, context: Context) : User {
        val dbHelper = DataBaseHelper(context)
        return dbHelper.getUserByUsername(username)
    }

    fun logIn(ID: Int, context: Context){
        val dbHelper = DataBaseHelper(context)
        dbHelper.setLoggedIn(ID)
    }

}