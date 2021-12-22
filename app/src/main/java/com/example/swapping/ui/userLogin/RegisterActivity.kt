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
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.Models.User
import com.example.swapping.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var DBHelper: DataBaseHelper
    private lateinit var registerViewModel: RegisterViewModel

    private lateinit var emailLayout: TextInputLayout
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var signUpButton: Button
    private lateinit var permissionCheck: CheckBox
    private lateinit var dateOfBirthCheckBox: CheckBox
    private lateinit var phoneNumberLayout: TextInputLayout
    private lateinit var cityLayout: TextInputLayout

    private val networkConnection = NetworkConnection()

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
            this.value = registerViewModel.validateForm(email, password, username, birthDate, permission)
        }

        addSource(passwordLiveData) { password ->
            val email = emailLiveData.value
            val username = usernameLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = registerViewModel.validateForm(email, password, username, birthDate, permission)
        }

        addSource(usernameLiveData) { username ->
            val email = emailLiveData.value
            val password = passwordLiveData.value
            val birthDate = birthDateLiveData.value
            val permission = permissionLiveData.value
            this.value = registerViewModel.validateForm(email, password, username, birthDate, permission)
        }

        addSource(birthDateLiveData) { birthDate ->
            val email = emailLiveData.value
            val username = usernameLiveData.value
            val password = passwordLiveData.value
            val permission = permissionLiveData.value
            this.value = registerViewModel.validateForm(email, password, username, birthDate, permission)
        }

        addSource(permissionLiveData) { permission ->
            val email = emailLiveData.value
            val username = usernameLiveData.value
            val password = passwordLiveData.value
            val birthDate = birthDateLiveData.value
            this.value = registerViewModel.validateForm(email, password, username, birthDate, permission)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        DBHelper = DataBaseHelper(applicationContext)
        registerViewModel = RegisterViewModel(DBHelper)

        emailLayout = findViewById(R.id.emailRegisterLayout)
        usernameLayout = findViewById(R.id.usernameRegisterLayout)
        passwordLayout = findViewById(R.id.passwordRegisterLayout)
        signUpButton = findViewById(R.id.registerButton)
        permissionCheck = findViewById(R.id.permissionCheckBox)
        dateOfBirthCheckBox = findViewById(R.id.dateOfBirthCheckBox)
        phoneNumberLayout = findViewById(R.id.phoneRegisterLayout)
        cityLayout = findViewById(R.id.cityRegisterLayout)
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


        signUpButton.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do internetu",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            } else {
                var fine = false

                isValidLiveData.observe(this) { isValid ->
                    fine = registerViewModel.registerErrors(
                        isValid,
                        dateOfBirthCheckBox,
                        permissionCheck,
                        emailLayout,
                        passwordLayout,
                        usernameLayout
                    )
                }

                if (fine) {
                    val username = findViewById<TextView>(R.id.usernameRegister)
                    val email = findViewById<TextView>(R.id.emailRegister)
                    val name = findViewById<TextView>(R.id.nameRegister)
                    val phoneNumber = findViewById<TextView>(R.id.phoneRegister)
                    val city = findViewById<TextView>(R.id.cityRegister)
                    val password = findViewById<TextView>(R.id.passwordRegister)

                    val user = User(
                        username = username.text.toString(),
                        email = email.text.toString(),
                        name = name.text.toString(),
                        phone_number = phoneNumber.text.toString(),
                        city = city.text.toString(),
                        password = password.text.toString()
                    )

                    DBHelper.addUser(user)

                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                }
            }
        }
    }
}