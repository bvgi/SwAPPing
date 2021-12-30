package com.example.swapping.ui.newAd

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.swapping.Models.Ad
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate


class NewAdActivity : AppCompatActivity() {
    private lateinit var newAdViewModel: NewAdViewModel
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_CAMERA = 1
    private lateinit var imageView: ImageView
    private var userID: Int = 0
    private lateinit var image: ByteArray
    private val networkConnection = NetworkConnection()

    val arg: NewAdActivityArgs by navArgs()

    private val titleLiveData = MutableLiveData<String>()
    private val descriptionLiveData = MutableLiveData<String>()
    private val voivodeshipLiveData = MutableLiveData<String>()
    private val categoryLiveData = MutableLiveData<String>()
    private val statusLiveData = MutableLiveData<String>()
    private val isValidLiveData = MediatorLiveData<HashMap<String, Boolean>>().apply {
        this.value =
            hashMapOf(
                "Title" to false,
                "Description" to false,
                "Voivodeship" to false,
                "Category" to false,
                "Status" to false
            )

        addSource(titleLiveData) { title ->
            val description = descriptionLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val category = categoryLiveData.value
            val status = statusLiveData.value
            this.value = newAdViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(descriptionLiveData) { description ->
            val title = titleLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val category = categoryLiveData.value
            val status = statusLiveData.value
            this.value = newAdViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(voivodeshipLiveData) { voivodeship ->
            val title = titleLiveData.value
            val description = descriptionLiveData.value
            val category = categoryLiveData.value
            val status = statusLiveData.value
            this.value = newAdViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(categoryLiveData) { category ->
            val title = titleLiveData.value
            val description = descriptionLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val status = statusLiveData.value
            this.value = newAdViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(statusLiveData) { status ->
            val title = titleLiveData.value
            val description = descriptionLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val category = categoryLiveData.value
            this.value = newAdViewModel.validateForm(title, description, voivodeship, category, status)
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_ad)

        newAdViewModel =
            ViewModelProvider(this).get(NewAdViewModel::class.java)

        val titleLayout = findViewById<TextInputLayout>(R.id.addTitleLayout)
        val descriptionLayout = findViewById<TextInputLayout>(R.id.addDescriptionLayout)
        val voivodeshipLayout = findViewById<TextInputLayout>(R.id.addLocationVoivodeshipLayout)
        val categoryLayout = findViewById<TextInputLayout>(R.id.addCategoryLayout)
        val statusLayout = findViewById<TextInputLayout>(R.id.addStatusLayout)

        titleLayout.editText?.doOnTextChanged{ text, _, _, _ ->
            titleLiveData.value = text?.toString()
        }

        descriptionLayout.editText?.doOnTextChanged{ text, _, _, _ ->
            descriptionLiveData.value = text?.toString()
        }

        val radioCategoryGroup = findViewById<RadioGroup>(R.id.radioGroupCategory)
        val radioStatusGroup = findViewById<RadioGroup>(R.id.radioGroupStatus)
        val title = findViewById<TextView>(R.id.addTitle)
        val description = findViewById<TextView>(R.id.addDescription)
        val city = findViewById<TextView>(R.id.addLocationCity)
        val addImage = findViewById<Button>(R.id.addPhoto)
        val addAnnouncement = findViewById<Button>(R.id.addAnnouncementButton)

        imageView = findViewById(R.id.addedPhoto)
        userID = arg.userID

        image = byteArrayOf()

        addImage.setOnClickListener {
            cameraRequest()
        }

        val dropdownVoivodeship: Spinner = findViewById(R.id.spinnerVoivodeship)
        val voivodeships = newAdViewModel.getVoivodeships(this)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf(" ", *voivodeships)
        )
        dropdownVoivodeship.adapter = adapter

        voivodeshipLiveData.value = dropdownVoivodeship.selectedItem.toString()

        val categories = newAdViewModel.getCategories(this)
        var catIndex = 0

        for(button in radioCategoryGroup.children){
            radioCategoryGroup.findViewById<RadioButton>(button.id).text = categories[catIndex]
            catIndex++
        }

        val statuses = newAdViewModel.getStatuses(this)
        var statIndex = 0

        for(button in radioStatusGroup.children){
            radioStatusGroup.findViewById<RadioButton>(button.id).text = statuses[statIndex]
            statIndex++
        }

        radioCategoryGroup.setOnCheckedChangeListener { _, checkedId ->
            categoryLiveData.value = radioCategoryGroup.findViewById<RadioButton>(checkedId).text.toString()
        }

        radioStatusGroup.setOnCheckedChangeListener { _, checkedId ->
            statusLiveData.value = radioStatusGroup.findViewById<RadioButton>(checkedId).text.toString()
        }

        addAnnouncement.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                var fine = false
                voivodeshipLiveData.value = dropdownVoivodeship.selectedItem.toString()
                isValidLiveData.observe(this) { isValid ->
                    fine = newAdViewModel.registerErrors(
                        isValid,
                        titleLayout,
                        descriptionLayout,
                        voivodeshipLayout,
                        categoryLayout,
                        statusLayout
                    )
                }
                if (fine) {
                    var cityName = "-"
                    if (!city.text.isNullOrBlank())
                        cityName = city.text.toString()

                    if (image.isEmpty()) {
                        val noPhoto =
                            resources.getDrawable(R.drawable.ic_baseline_no_photography_24, null)
                        imageView.setImageResource(R.drawable.ic_baseline_no_photography_24)
                        image = newAdViewModel.getBytes(imageView, noPhoto)
                    }
                    val ad = Ad(
                        user = userID,
                        title = title.text.toString(),
                        description = description.text.toString(),
                        voivodeship = dropdownVoivodeship.selectedItem.toString(),
                        category = radioCategoryGroup.findViewById<RadioButton>(
                            radioCategoryGroup.checkedRadioButtonId
                        ).text.toString(),
                        city = cityName,
                        status = radioStatusGroup.findViewById<RadioButton>(radioStatusGroup.checkedRadioButtonId).text.toString(),
                        image = image,
                        published_date = LocalDate.now().year * 10000 + LocalDate.now().monthValue * 100 + LocalDate.now().dayOfMonth,
                        negotiation = 0,
                        archived = 0
                    )
                    newAdViewModel.addAd(ad, this)

                    Snackbar.make(
                        findViewById(R.id.myCoordinatorLayout),
                        "Ogłoszenie zostało dodane",
                        Snackbar.LENGTH_SHORT
                    ).show()

                    val homeIntent = Intent(this, MainActivity::class.java)
                    homeIntent.putExtra("userID", userID)
                    startActivity(homeIntent)
                }
            }
        }
    }

    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                findViewById(R.id.myCoordinatorLayout),
                "Błąd podczas uruchamiania kamery",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun cameraRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE
            )
        } else {
            startCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null){
            val imageBitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            image = newAdViewModel.getBytes(imageView, null)
            imageView.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCamera()
            }
            else
            {
                Toast.makeText(
                    this,
                    "Odmówiono dostępu do aparatu",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtra("userID", userID)
    }
}