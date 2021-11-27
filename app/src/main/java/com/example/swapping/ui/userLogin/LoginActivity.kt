package com.example.swapping.ui.userLogin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.swapping.databinding.ActivityLoginBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.swapping.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    private val emailLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val isValidLiveData = MediatorLiveData<Int>().apply {
        this.value = 0

        addSource(emailLiveData) { email ->
            val password = passwordLiveData.value
            this.value = validateForm(email, password)
        }

        addSource(passwordLiveData) { password ->
            val email = emailLiveData.value
            this.value = validateForm(email, password)
        }
    }

    private fun validateForm(email: String?, password: String?): Int {
        val isValidEmail = email != null && email.isNotBlank() && email.contains("@") && email.matches(Regex(".+\\..+"))
        val isValidPassword = password != null && password.isNotBlank() && password.length >= 6
        return if (isValidEmail && isValidPassword) 0
        else if (!isValidPassword && !isValidEmail) 1
        else if (!isValidEmail) 2
        else 3
    }

    private lateinit var LoginViewModel: UserProfileViewModel
//    private var binding: ActivityLoginBinding? = null
    private lateinit var makeAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        LoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)

        val emailLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passLayout)
        val information = findViewById<TextView>(R.id.verifyLoginData)
        val signInButton = findViewById<Button>(R.id.buttonLogin)

        emailLayout.editText?.doOnTextChanged { text, _, _, _ ->
            emailLiveData.value = text?.toString()
        }

        passwordLayout.editText?.doOnTextChanged {text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        signInButton.setOnClickListener {
                isValidLiveData.observe(this) { isValid ->
                    Log.d("Valid", isValid.toString())
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
                            signInButton.setOnClickListener {
                                setContentView(R.layout.activity_main)
                            }
                        }
                    }
                }
        }

        makeAccount = findViewById(R.id.registerLink)
        makeAccount.setOnClickListener {
            setContentView(R.layout.activity_register)
        }




    }

//    override fun onCreate(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        userProfileViewModel =
//                ViewModelProvider(this).get(UserProfileViewModel::class.java)
//
//        _binding = ActivityLoginBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val login: EditText = binding.loginEmail
//        val passwd: EditText = binding.password
//
////        userProfileViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
//        return root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val registerView: TextView = binding.registerLink
//        registerView.setOnClickListener {
//            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//            transaction.replace(R.id.login_fragment, RegisterFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}