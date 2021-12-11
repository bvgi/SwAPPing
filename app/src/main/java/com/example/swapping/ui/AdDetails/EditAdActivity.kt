package com.example.swapping.ui.AdDetails

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.core.view.children
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.Models.Ad
import com.example.swapping.Models.User
import com.example.swapping.R
import java.io.ByteArrayOutputStream
import java.time.LocalDate

class EditAdActivity : AppCompatActivity() {
    private var adID = 0

    private lateinit var editAdViewModel: EditAdViewModel

    private lateinit var saveDataItem: MenuItem
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var addedPhoto: ImageView
    private lateinit var changePhotoButton: Button
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var city: EditText
    private lateinit var voivodeship: Spinner
    private lateinit var category: RadioGroup
    private lateinit var status: RadioGroup
    private lateinit var changedPhoto: ByteArray

    private lateinit var adDetails: Ad

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_CAMERA = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ad)

        editAdViewModel = EditAdViewModel()

        val extras: Bundle? = intent.extras
        if(extras != null){
            adID = extras.getInt("adID")
        }



        dbHelper = DataBaseHelper(this)
        adDetails = dbHelper.getAnnouncement(adID)
        val photo = editAdViewModel.getImage(adDetails.image)

        changedPhoto = adDetails.image

        addedPhoto = findViewById(R.id.adPhoto)
        addedPhoto.setImageBitmap(photo)

        changePhotoButton = findViewById(R.id.changePhoto)
        changePhotoButton.setOnClickListener {
            cameraRequest()
        }

        title = findViewById(R.id.editAdTitle)
        title.setText(adDetails.title)

        description = findViewById(R.id.editDescription)
        description.setText(adDetails.description)

        city = findViewById(R.id.editLocationCity)
        if (adDetails.city == "-")
            city.setText("")
        else
            city.setText(adDetails.city)


        voivodeship = findViewById(R.id.spinnerVoivodeship)
        val voivodeships = dbHelper.getVoivodeships()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, voivodeships)
        voivodeship.adapter = adapter

        voivodeship.setSelection(adapter.getPosition(adDetails.voivodeship))

        category = findViewById(R.id.editRadioGroupCategory)
        val categories = dbHelper.getCategories()
        var catIndex = 0
        var currCheckedCategory = 0

        for(button in category.children){
            category.findViewById<RadioButton>(button.id).text = categories[catIndex]
            if (categories[catIndex] == adDetails.category)
                currCheckedCategory = button.id
            catIndex++
        }

        category.findViewById<RadioButton>(currCheckedCategory).isChecked = true

        status = findViewById(R.id.editRadioGroupStatus)
        val statuses = dbHelper.getStatuses()
        var statIndex = 0
        var currCheckedStatus = 0

        for(button in status.children){
            status.findViewById<RadioButton>(button.id).text = statuses[statIndex]
            if (statuses[statIndex] == adDetails.status)
                currCheckedStatus = button.id
            statIndex++
        }

        status.findViewById<RadioButton>(currCheckedStatus).isChecked = true

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

                var cityName = "-"
                if (!city.text.isNullOrBlank())
                    cityName = city.text.toString()

                if(changedPhoto.isEmpty()){
                    val nophoto = resources.getDrawable(R.drawable.ic_baseline_no_photography_24, null)
                    addedPhoto.setImageResource(R.drawable.ic_baseline_no_photography_24)
                    val bitmap =  addedPhoto.drawable.toBitmap(nophoto.intrinsicWidth, nophoto.intrinsicHeight)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
                    val bitmapdata = stream.toByteArray()
                    changedPhoto = bitmapdata

                }

                println("Ad Details ::: " + adDetails.user)

                val ad = Ad(
                    ID = adDetails.ID,
                    user = adDetails.user,
                    title = title.text.toString(),
                    description = description.text.toString(),
                    voivodeship = voivodeship.selectedItem.toString(),
                    category = category.findViewById<RadioButton>(
                        category.checkedRadioButtonId
                    ).text.toString(),
                    city = cityName,
                    status = status.findViewById<RadioButton>(status.checkedRadioButtonId).text.toString(),
                    image = changedPhoto,
                    published_date = adDetails.published_date,
                    negotiation = 0,
                    archived = 0
                )
                println(ad)
                dbHelper.updateAnnouncement(ad)

//                val index = bundleOf("userID" to userID)
//                navCon.navigate(R.id.navigation_profile, index)

                val intent = Intent(this, AdDetailsActivity::class.java)
                intent.putExtras(bundleOf("userID" to adDetails.user, "profileID" to adDetails.user, "adID" to adDetails.ID))
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE && data != null){
            val imageBitmap = data.extras?.get("data") as Bitmap
            addedPhoto.setImageBitmap(imageBitmap)
            changedPhoto = getBytes(imageBitmap)
//            val compressed = imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream)
        }
    }

    fun getBytes(bitmap: Bitmap): ByteArray {
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(CompressFormat.PNG, 0, stream)
//        return stream.toByteArray()
        val bitmap =  addedPhoto.drawable.toBitmap(addedPhoto.drawable.intrinsicWidth, addedPhoto.drawable.intrinsicHeight)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
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
        return super.getParentActivityIntent()?.putExtras( bundleOf("userID" to adDetails.user, "profileID" to adDetails.user, "adID" to adDetails.ID))
    }
}