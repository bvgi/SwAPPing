package com.example.swapping.ui.userLogin

import androidx.lifecycle.ViewModel
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

}