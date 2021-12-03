package com.example.swapping.ui.userLogin

import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginViewModel : ViewModel() {

    fun loginErrors(isValid: Int, emailLayout: TextInputLayout, passwordLayout: TextInputLayout){
        when (isValid) {
            2 -> {
                passwordLayout.error = null
                emailLayout.error = "Niepoprawny adres e-mail"
            }
            3 -> {
                emailLayout.error = null
                passwordLayout.error = "Niepoprawne hasło"
            }
            1 -> {
                emailLayout.error = "Niepoprawny adres e-mail"
                passwordLayout.error = "Niepoprawne hasło"
            }
            else -> {
                emailLayout.error = null
                passwordLayout.error = null

            }
        }
    }

}