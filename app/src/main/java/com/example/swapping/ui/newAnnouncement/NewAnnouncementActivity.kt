package com.example.swapping.ui.newAnnouncement

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.Models.Announcement
import com.example.swapping.R
import com.example.swapping.databinding.FragmentNewAnnouncementBinding
import com.example.swapping.ui.profile.ProfileFragmentDirections
import com.example.swapping.ui.profile.ProfileViewFragmentArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream
import java.time.LocalDate

class NewAnnouncementActivity : AppCompatActivity() {
    private lateinit var newAnnouncementViewModel: NewAnnouncementViewModel
    private var _binding: FragmentNewAnnouncementBinding? = null
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_CAMERA = 1
    private lateinit var imageView: ImageView
    private var userID: Int = 0

    val arg: NewAnnouncementActivityArgs by navArgs()

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
            this.value = newAnnouncementViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(descriptionLiveData) { description ->
            val title = titleLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val category = categoryLiveData.value
            val status = statusLiveData.value
            this.value = newAnnouncementViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(voivodeshipLiveData) { voivodeship ->
            val title = titleLiveData.value
            val description = descriptionLiveData.value
            val category = categoryLiveData.value
            val status = statusLiveData.value
            this.value = newAnnouncementViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(categoryLiveData) { category ->
            val title = titleLiveData.value
            val description = descriptionLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val status = statusLiveData.value
            this.value = newAnnouncementViewModel.validateForm(title, description, voivodeship, category, status)
        }

        addSource(statusLiveData) { status ->
            val title = titleLiveData.value
            val description = descriptionLiveData.value
            val voivodeship = voivodeshipLiveData.value
            val category = categoryLiveData.value
            this.value = newAnnouncementViewModel.validateForm(title, description, voivodeship, category, status)
        }

    }

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_announcement)

        newAnnouncementViewModel =
            ViewModelProvider(this).get(NewAnnouncementViewModel::class.java)

        val DBHelper = DataBaseHelper(applicationContext)

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
        val voivodeship = findViewById<TextView>(R.id.addLocationVoivodeship)
        val addImage = findViewById<Button>(R.id.addPhoto)
        val addAnnouncement = findViewById<Button>(R.id.addAnnouncementButton)

        imageView = findViewById(R.id.addedPhoto)
        userID = arg.userID
        println("AGRS $userID")

        var image = byteArrayOf()

        addImage.setOnClickListener {
            cameraRequest()

            image = getByteArray(imageView)
        }

        val dropdownVoivodeship: Spinner = findViewById(R.id.spinnerVoivodeship)
        val voivodeships = DBHelper.getVoivodeships()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayOf<String>(" ", *voivodeships))
        dropdownVoivodeship.adapter = adapter

        voivodeshipLiveData.value = dropdownVoivodeship.selectedItem.toString()

        val categories = DBHelper.getCategories()
        var index = 0

        for(button in radioCategoryGroup.children){
            radioCategoryGroup.findViewById<RadioButton>(button.id).text = categories[index]
            index++
        }

        val statuses = DBHelper.getStatuses()
        index = 0

        for(button in radioStatusGroup.children){
            radioStatusGroup.findViewById<RadioButton>(button.id).text = statuses[index]
            index++
        }

        radioCategoryGroup.setOnCheckedChangeListener { _, checkedId ->
            categoryLiveData.value = radioCategoryGroup.findViewById<RadioButton>(checkedId).text.toString()
        }

        radioStatusGroup.setOnCheckedChangeListener { _, checkedId ->
            statusLiveData.value = radioStatusGroup.findViewById<RadioButton>(checkedId).text.toString()
        }

        addAnnouncement.setOnClickListener {
            var fine = false
            voivodeshipLiveData.value = dropdownVoivodeship.selectedItem.toString()
            isValidLiveData.observe(this) { isValid ->
                fine = newAnnouncementViewModel.registerErrors(
                    isValid,
                    titleLayout,
                    descriptionLayout,
                    voivodeshipLayout,
                    categoryLayout,
                    statusLayout
                )
                println(" ${titleLiveData.value},  ${descriptionLiveData.value}, ${voivodeshipLiveData.value}, ${categoryLiveData.value}, ${statusLiveData.value}")
            }
            if (fine) {
                val ann = Announcement(
                    user = userID,
                    title = title.text.toString(),
                    description = description.text.toString(),
                    voivodeship = dropdownVoivodeship.selectedItem.toString(),
                    category = radioCategoryGroup.findViewById<RadioButton>(
                        radioCategoryGroup.checkedRadioButtonId
                    ).text.toString(),
                    city = city.text.toString(),
                    status = radioStatusGroup.findViewById<RadioButton>(radioStatusGroup.checkedRadioButtonId).text.toString(),
                    image = image,
                    published_date = LocalDate.now().year * 10000 + LocalDate.now().monthValue * 100 + LocalDate.now().dayOfMonth,
                    negotiation = 0,
                    archived = 0
                )
                DBHelper.addAnnouncement(ann)

                println(ann)

                Snackbar.make(
                    findViewById(R.id.myCoordinatorLayout),
                    "Ogłoszenie zostało dodane",
                    Snackbar.LENGTH_SHORT
                ).show()


                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.putExtra("userid", userID)
                startActivity(homeIntent)

            }
        }




//        _binding = FragmentNewAnnouncementBinding.inflate(inflater, container, false)
//        binding.root.context
//        val root: View = binding.root

//        val title: TextView = binding.addTitle
//
//        newAnnouncementViewModel.text.observe(viewLifecycleOwner, Observer {
//            title.text = it
//        })
//        if ()
    }

    private fun startCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            println("ERROR")
        }


    }

    private fun cameraRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA
            )
        }
        else
            startCamera()

    }

    fun getByteArray(imageView: ImageView): ByteArray {
        return try {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageByteArray = stream.toByteArray()
            stream.close()
            imageByteArray
        } catch (e: Exception) {
            e.printStackTrace()
            byteArrayOf(1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null){
            imageView.setImageBitmap(data.extras?.get("data") as Bitmap)
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
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }



    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtra("userid", userID)
    }
}