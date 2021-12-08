package com.example.swapping.ui.userLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.R
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var LoginViewModel: LoginViewModel
    private lateinit var DBHelper: DataBaseHelper
    //    private var binding: ActivityLoginBinding? = null
    private lateinit var makeAccount: TextView

    private val usernameLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val isValidLiveData = MediatorLiveData<Int>().apply {
        this.value = 1

        addSource(usernameLiveData) { username ->
            val password = passwordLiveData.value
            this.value = validateForm(username, password)
        }

        addSource(passwordLiveData) { password ->
            val username = usernameLiveData.value
            this.value = validateForm(username, password)
        }
    }

    fun validateForm(username: String?, password: String?): Int {
        val isValidUsername = username != null && username.isNotBlank() && DBHelper.getUserByUsername(username).ID > -1
        val isValidPassword = password != null && password.isNotBlank() && password.length >= 6 && DBHelper.getUserByUsername(username!!).password == password
        Log.d("Validate", isValidUsername.toString() + isValidPassword.toString())
        return if (isValidUsername && isValidPassword) 0
        else if (!isValidPassword && !isValidUsername) 1
        else if (!isValidUsername) 2
        else 3
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        LoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login)
        LoginViewModel = LoginViewModel()
        DBHelper = DataBaseHelper(applicationContext)
        DBHelper.addCategories()
        DBHelper.addVoivodeships()
        DBHelper.addStatuses()
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
            var fine = false
            isValidLiveData.observe(this) { isValid ->
                LoginViewModel.loginErrors(isValid, usernameLayout, passwordLayout)
                if (isValid == 0)
                    fine = true
            }
            if(fine) {
                val userID = DBHelper.getUserByUsername(usernameLiveData.value.toString()).ID
                DBHelper.setLoggedIn(userID)

                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.putExtra("userid", userID)
                startActivity(homeIntent)
            }

        }


        makeAccount = findViewById(R.id.registerLink)
        makeAccount.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }
    }


//    override fun onCreate(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        userProfileViewModel =
//                ViewModelProvider(this).get(RegisterViewModel::class.java)
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