package com.example.swapping.ui.searching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.google.android.material.snackbar.Snackbar

class SortActivity : AppCompatActivity() {
    private lateinit var dataASC: RadioButton
    private lateinit var dataDESC: RadioButton
    private lateinit var rateASC: RadioButton
    private lateinit var rateDESC: RadioButton
    private lateinit var button: Button
    private lateinit var radioGroup: RadioGroup

    private val networkConnection = NetworkConnection()

    private var userID = 0
    private var category = ""
    private var voivodeship = ""
    private var sort = 0
    private var query = ""
    private lateinit var filter: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort)
        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            category = extras.getString("category").toString()
            voivodeship = extras.getString("voivodeship").toString()
            sort = extras.getInt("sort")
            query = extras.getString("query").toString()
            if(extras.get("filter") != null)
                filter = extras.get("filter") as HashMap<String, String>
            else
                filter = hashMapOf()
        }

        dataASC = findViewById(R.id.dateASC)
        dataDESC = findViewById(R.id.dateDESC)
        rateASC = findViewById(R.id.rateASC)
        rateDESC = findViewById(R.id.rateDESC)

        when(sort){
            1 -> dataASC.isChecked = true
            2 -> dataDESC.isChecked = true
            3 -> rateASC.isChecked = true
            4 -> rateDESC.isChecked = true
            else -> {
                dataASC.isChecked = false
                dataDESC.isChecked = false
                rateASC.isChecked = false
                rateDESC.isChecked = false
            }
        }

        radioGroup = findViewById(R.id.sortingGroup)

        button = findViewById(R.id.confirmSort)
        button.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, ResultsSearchActivity::class.java)
                if (dataASC.isChecked) {
                    intent.putExtras(
                        bundleOf(
                            "query" to query,
                            "userID" to userID,
                            "category" to category,
                            "voivodeship" to voivodeship,
                            "sort" to 1,
                            "filterS" to filter["S"],
                            "filterR" to filter["R"],
                            "filterC" to filter["C"]
                        )
                    )
                } else if (dataDESC.isChecked) {
                    intent.putExtras(
                        bundleOf(
                            "query" to query,
                            "userID" to userID,
                            "category" to category,
                            "voivodeship" to voivodeship,
                            "sort" to 2,
                            "filterS" to filter["S"],
                            "filterR" to filter["R"],
                            "filterC" to filter["C"]
                        )
                    )
                } else if (rateASC.isChecked) {
                    intent.putExtras(
                        bundleOf(
                            "query" to query,
                            "userID" to userID,
                            "category" to category,
                            "voivodeship" to voivodeship,
                            "sort" to 3,
                            "filterS" to filter["S"],
                            "filterR" to filter["R"],
                            "filterC" to filter["C"]
                        )
                    )
                } else if (rateDESC.isChecked) {
                    intent.putExtras(
                        bundleOf(
                            "query" to query,
                            "userID" to userID,
                            "category" to category,
                            "voivodeship" to voivodeship,
                            "sort" to 4,
                            "filterS" to filter["S"],
                            "filterR" to filter["R"],
                            "filterC" to filter["C"]
                        )
                    )
                } else {
                    intent.putExtras(
                        bundleOf(
                            "query" to query,
                            "userID" to userID,
                            "category" to category,
                            "voivodeship" to voivodeship,
                            "sort" to 0,
                            "filterS" to filter["S"],
                            "filterR" to filter["R"],
                            "filterC" to filter["C"]
                        )
                    )
                }
                startActivity(intent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.reset_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.resetAll -> {
                radioGroup.clearCheck()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}