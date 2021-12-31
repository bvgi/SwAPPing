package com.example.swapping.ui.userLogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.swapping.ui.MainActivity
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var makeAccount: TextView

    private val networkConnection = NetworkConnection()

    private val usernameLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val isValidLiveData = MediatorLiveData<Int>().apply {
        this.value = 1

        addSource(usernameLiveData) { username ->
            val password = passwordLiveData.value
            this.value = loginViewModel.validateForm(username, password, applicationContext)
        }

        addSource(passwordLiveData) { password ->
            val username = usernameLiveData.value
            this.value = loginViewModel.validateForm(username, password, applicationContext)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val usernameLayout = findViewById<TextInputLayout>(R.id.usernameLoginLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordLoginLayout)
        val signInButton = findViewById<Button>(R.id.buttonLogin)


        usernameLayout.editText?.doOnTextChanged { text, _, _, _ ->
            usernameLiveData.value = text?.toString()
        }

        passwordLayout.editText?.doOnTextChanged {text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        signInButton.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                var fine = false
                isValidLiveData.observe(this) { isValid ->
                    loginViewModel.loginErrors(isValid, usernameLayout, passwordLayout)
                    if (isValid == 0)
                        fine = true
                }
                if (fine) {
                    val userID = loginViewModel.getUser(usernameLiveData.value.toString(), this).ID
                    loginViewModel.logIn(userID, this)

                    val homeIntent = Intent(this, MainActivity::class.java)
                    homeIntent.putExtra("userID", userID)
                    startActivity(homeIntent)
                }
            }
        }

        makeAccount = findViewById(R.id.registerLink)
        makeAccount.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}