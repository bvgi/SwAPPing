package com.example.swapping.ui.userLogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.swapping.DataBase.UserDataBaseHelper
import com.example.swapping.Models.User
import com.example.swapping.R
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private val userDBHelper = UserDataBaseHelper(applicationContext)
    private val emailLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val usernameLiveData = MutableLiveData<String>()
    private val birthDateLiveData = MutableLiveData<Boolean>()
    private val nameLiveData = MutableLiveData<String>()
    private val phoneNumberLiveData = MutableLiveData<String>()
    private val cityLiveData = MutableLiveData<String>()
    private val permissionLiveData = MutableLiveData<Boolean>()
    private val isValidLiveData = MediatorLiveData<HashMap<String, Boolean>>().apply {
        this.value =
            hashMapOf(
            "Email" to false,
            "Password" to false,
            "Username" to false,
            "BirthDate" to false,
            "Permission" to false
        )

        addSource(emailLiveData) { email ->
            val password = passwordLiveData.value
            val username = usernameLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, username, birthDate, permission)
        }

        addSource(passwordLiveData) { password ->
            val email = emailLiveData.value
            val username = usernameLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, username, birthDate, permission)
        }

        addSource(usernameLiveData) { username ->
            val email = emailLiveData.value
            val password = passwordLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, username, birthDate, permission)
        }

        addSource(birthDateLiveData) { birthDate ->
            val email = emailLiveData.value
            val username = usernameLiveData.value
            val password = passwordLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, username, birthDate, permission)
        }

        addSource(permissionLiveData) { permission ->
            val email = emailLiveData.value
            val username = usernameLiveData.value
            val password = passwordLiveData.value
            val birthDate = birthDateLiveData.value
            this.value = validateForm(email, password, username, birthDate, permission)
        }

    }

    private fun validateForm(
        email: String?,
        password: String?,
        name: String?,
        birthDate: Boolean?,
        permission: Boolean?
    ): HashMap<String, Boolean>  {
        val isValidEmail =
            email != null && email.isNotBlank() && email.contains("@") && email.contains(".")
        val isValidPassword = password != null && password.isNotBlank() && password.length >= 6
        val isValidNickName = name != null && name.isNotBlank() // && name.notIn
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

    private lateinit var registerViewModel: RegisterViewModel
    //    private var binding: ActivityLoginBinding? = null
    private lateinit var makeAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailLayout = findViewById<TextInputLayout>(R.id.emailRegisterLayout)
        val usernameLayout = findViewById<TextInputLayout>(R.id.usernameRegisterLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordRegisterLayout)
        val signUpButton = findViewById<Button>(R.id.registerButton)
        val permissionCheck = findViewById<CheckBox>(R.id.permissionCheckBox)
        val dateOfBirthCheckBox = findViewById<CheckBox>(R.id.dateOfBirthCheckBox)
        val phoneNumberLayout = findViewById<TextInputLayout>(R.id.phoneRegisterLayout)
        val cityLayout = findViewById<TextInputLayout>(R.id.cityRegisterLayout)
        var permissionValue = false

        emailLayout.editText?.doOnTextChanged { text, _, _, _ ->
            emailLiveData.value = text?.toString()
        }

        passwordLayout.editText?.doOnTextChanged {text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        usernameLayout.editText?.doOnTextChanged {text, _, _, _ ->
            usernameLiveData.value = text?.toString()
        }

        permissionCheck.setOnClickListener {
            permissionValue = !permissionValue
            permissionLiveData.value = permissionValue
        }

        dateOfBirthCheckBox.setOnCheckedChangeListener { _, isChecked ->
            birthDateLiveData.value = isChecked
        }

        phoneNumberLayout.editText?.doOnTextChanged{text, _, _, _ ->
            phoneNumberLiveData.value = text.toString()
        }

        cityLayout.editText?.doOnTextChanged{text, _, _, _ ->
            cityLiveData.value = text.toString()
        }

        val user = User(username = usernameLiveData.value.toString(),
                        email = emailLiveData.value.toString(),
                        name = nameLiveData.value.toString(),
                        phone_number = phoneNumberLiveData.value.toString().toInt(),
                        city = cityLiveData.value.toString()
        )

        signUpButton.setOnClickListener {
            isValidLiveData.observe(this) { isValid ->
                registerViewModel.registerErrors(isValid, dateOfBirthCheckBox, permissionCheck, emailLayout, passwordLayout, usernameLayout)
                if (!isValid.containsValue(false)){
                    signUpButton.setOnClickListener {
//                        userDBHelper.addUser(user, passwordLiveData.value.toString())
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                    }
                }

            }
        }

    }

}