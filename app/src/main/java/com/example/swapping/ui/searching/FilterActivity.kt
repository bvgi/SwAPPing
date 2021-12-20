package com.example.swapping.ui.searching

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.children
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.MainActivity
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.EditAdActivity

class FilterActivity : AppCompatActivity() {
    private lateinit var statusTitle: LinearLayout
    private lateinit var categoryTitle: LinearLayout
    private lateinit var rateTitle: LinearLayout
    private lateinit var status: RadioGroup
    private lateinit var category: RadioGroup
    private lateinit var rate: RadioGroup
    private lateinit var confirmButton: Button

    private var userID = 0
    private lateinit var filter: HashMap<String, String>
    private var categoryList = ""
    private var voivodeship = ""
    private var sort = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            categoryList = extras.getString("category").toString()
            voivodeship = extras.getString("voivodeship").toString()
            filter = extras.get("filter") as HashMap<String, String>
            sort = extras.getInt("sort")
        }

        val dbHelper = DataBaseHelper(this)

        statusTitle = findViewById(R.id.statusLayout)
        status = findViewById(R.id.status)

        val statuses = dbHelper.getStatuses()
        for(index in statuses.indices){
            val button = RadioButton(this)
            button.id = index
            button.text = statuses[index]
            status.addView(button)
        }

        if(filter["S"] != "null") {
            for(child in status.children){
                if(status.findViewById<RadioButton>(child.id).text == filter["S"]){
                    status.findViewById<RadioButton>(child.id).isChecked = true
                }
            }
        }

        statusTitle.setOnClickListener {
            if(status.visibility == View.GONE)
                status.visibility = View.VISIBLE
            else
                status.visibility = View.GONE
        }


        categoryTitle = findViewById(R.id.categoryLayout)
        category = findViewById(R.id.category)

        if (categoryList != "")
            categoryTitle.visibility = View.GONE

        val categories = dbHelper.getCategories()
        for(index in categories.indices){
            val button = RadioButton(this)
            button.id = index
            button.text = categories[index]
            category.addView(button)
        }
        categoryTitle.setOnClickListener {
            if(category.visibility == View.GONE)
                category.visibility = View.VISIBLE
            else
                category.visibility = View.GONE
        }

        if(filter["C"] != "null") {
            for(child in category.children){
                if(category.findViewById<RadioButton>(child.id).text == filter["C"]){
                    category.findViewById<RadioButton>(child.id).isChecked = true
                }
            }
        }

        rateTitle = findViewById(R.id.rateLayout)
        rate = findViewById(R.id.rate)

        for(index in 0..4){
            val button = RadioButton(this)
            button.id = index
            if(index < 4)
                button.text = "${index+1} i więcej"
            else
                button.text = "${index+1}"
            rate.addView(button)
        }

        if(filter["R"] != "null") {
            for(child in rate.children){
                if(rate.findViewById<RadioButton>(child.id).text == filter["R"]){
                    rate.findViewById<RadioButton>(child.id).isChecked = true
                }
            }
        }

        rateTitle.setOnClickListener {

            if(rate.visibility == View.GONE)
                rate.visibility = View.VISIBLE
            else
                rate.visibility = View.GONE
        }

        confirmButton = findViewById(R.id.confirmFilter)
        confirmButton.setOnClickListener {
            val intent = Intent(this, ResultsSearchActivity::class.java)
            intent.putExtras(bundleOf(
                "userID" to userID,
                "category" to categoryList,
                "voivodeship" to voivodeship,
                "filterS" to if(status.checkedRadioButtonId != -1) status.findViewById<RadioButton>(status.checkedRadioButtonId).text.toString() else "null",
                "filterC" to if(category.checkedRadioButtonId != -1) category.findViewById<RadioButton>(category.checkedRadioButtonId).text.toString() else "null",
                "filterR" to if(rate.checkedRadioButtonId != -1) rate.findViewById<RadioButton>(rate.checkedRadioButtonId).text.toString() else "null",
                "sort" to sort))
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reset_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.resetAll -> {
                category.clearCheck()
                status.clearCheck()
                rate.clearCheck()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}