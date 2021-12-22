package com.example.swapping.ui.profile

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.Models.User
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editProfileViewModel: EditProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    private var userID: Int = 0
    private lateinit var userData: User
    private lateinit var DBHelper: DataBaseHelper
    private val networkConnection = NetworkConnection()

    private lateinit var emailLayout: TextInputLayout
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var phoneNumberLayout: TextInputLayout
    private lateinit var cityLayout: TextInputLayout
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var name: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var city: EditText

    private val args: EditProfileActivityArgs by navArgs()

    private lateinit var saveDataItem: MenuItem

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        DBHelper = DataBaseHelper(applicationContext)

        emailLayout = findViewById(R.id.emailEditLayout)
        usernameLayout = findViewById(R.id.usernameEditLayout)
        passwordLayout = findViewById(R.id.passwordEditLayout)
        phoneNumberLayout = findViewById(R.id.phoneEditLayout)
        cityLayout = findViewById(R.id.cityEditLayout)

        userID = args.userID

        userData = getUserData()

        email = findViewById(R.id.emailEdit)
        username = findViewById(R.id.usernameEdit)
        password = findViewById(R.id.passwordEdit)
        name = findViewById(R.id.nameEdit)
        phoneNumber = findViewById(R.id.phoneEdit)
        city = findViewById(R.id.cityEdit)

        email.setText(userData.email)
        username.setText(userData.username)
        name.setText(userData.name)
        phoneNumber.setText(userData.phone_number)
        city.setText(userData.city)

        editProfileViewModel =
            ViewModelProvider(this).get(EditProfileViewModel::class.java)

    }

    private fun getUserData() : User {
        return DBHelper.getUserById(userID)
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtra("userID", userID)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        if (menu != null)
            saveDataItem = menu.findItem(R.id.menu_saveData)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_saveData)
            saveDataItem = item

        when (item.itemId) {
            R.id.menu_saveData -> {
                val user: User
                if(password.text.isNullOrBlank())
                    user = User(
                        ID = userID,
                        email = email.text.toString(),
                        username = username.text.toString(),
                        name = name.text.toString(),
                        password = userData.password,
                        city = city.text.toString(),
                        phone_number = phoneNumber.text.toString()
                    )
                else
                    user = User(
                        ID = userID,
                        email = email.text.toString(),
                        username = username.text.toString(),
                        name = name.text.toString(),
                        password = password.text.toString(),
                        city = city.text.toString(),
                        phone_number = phoneNumber.text.toString()
                    )
                if (!networkConnection.isNetworkAvailable(applicationContext)) {
                    Snackbar.make(
                        findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    DBHelper.updateUser(user)
                    onBackPressed()
                }
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}