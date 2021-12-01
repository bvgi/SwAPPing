package com.example.swapping.ui.userLogin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.swapping.R
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private val emailLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val nicknameLiveData = MutableLiveData<String>()
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
            "Nickname" to false,
            "BirthDate" to false,
            "Permission" to false
        )

        addSource(emailLiveData) { email ->
            val password = passwordLiveData.value
            val nickname = nicknameLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, nickname, birthDate, permission)
        }

        addSource(passwordLiveData) { password ->
            val email = emailLiveData.value
            val nickname = nicknameLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, nickname, birthDate, permission)
        }

        addSource(nicknameLiveData) { nickname ->
            val email = emailLiveData.value
            val password = passwordLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, nickname, birthDate, permission)
        }

        addSource(birthDateLiveData) { birthDate ->
            val email = emailLiveData.value
            val nickname = nicknameLiveData.value
            val password = passwordLiveData.value
            val permission = permissionLiveData.value
            this.value = validateForm(email, password, nickname, birthDate, permission)
        }

        addSource(permissionLiveData) { permission ->
            val email = emailLiveData.value
            val nickname = nicknameLiveData.value
            val password = passwordLiveData.value
            val birthDate = birthDateLiveData.value
            this.value = validateForm(email, password, nickname, birthDate, permission)
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
            "Nickname" to isValidNickName
        )
    }

    private lateinit var RegisterViewModel: UserProfileViewModel
    //    private var binding: ActivityLoginBinding? = null
    private lateinit var makeAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailLayout = findViewById<TextInputLayout>(R.id.emailRegisterLayout)
        val nicknameLayout = findViewById<TextInputLayout>(R.id.nicknameRegisterLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordRegisterLayout)
        val signUpButton = findViewById<Button>(R.id.registerButton)
        val permissionCheck = findViewById<CheckBox>(R.id.permissionCheckBox)
        val dateOfBirthCheckBox = findViewById<CheckBox>(R.id.dateOfBirthCheckBox)
        var permissionValue = false



        emailLayout.editText?.doOnTextChanged { text, _, _, _ ->
            emailLiveData.value = text?.toString()
        }

        passwordLayout.editText?.doOnTextChanged {text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        nicknameLayout.editText?.doOnTextChanged {text, _, _, _ ->
            nicknameLiveData.value = text?.toString()
        }

        permissionCheck.setOnClickListener {
            permissionValue = !permissionValue
            permissionLiveData.value = permissionValue
        }

//        permissionCheck.setOnCheckedChangeListener { _, isChecked ->
//            permissionLiveData.value = isChecked
//        }

        dateOfBirthCheckBox.setOnCheckedChangeListener { _, isChecked ->
            birthDateLiveData.value = isChecked
        }

        signUpButton.setOnClickListener {
            isValidLiveData.observe(this) { isValid ->
                for((key,value) in isValid.entries) {
                    when (key) {
                        "BirthDate" -> {
                            if (!value) dateOfBirthCheckBox.error = "Obowiązkowe pole"
                            else dateOfBirthCheckBox.error = null
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
                        "Nickname" -> {
                            if (!value) nicknameLayout.error = "Nazwa użytkownika już istnieje lub jest niepoprawna"
                            else nicknameLayout.error = null
                        }
                    }
                }
                if (!isValid.containsValue(false)){
                    signUpButton.setOnClickListener {
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                    }
                }

            }
        }

    }

}