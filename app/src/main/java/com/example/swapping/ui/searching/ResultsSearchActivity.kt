package com.example.swapping.ui.searching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBaseHelper
import com.example.swapping.ui.MainActivity
import com.example.swapping.Models.Ad
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.home.HomeAdapter
import com.google.android.material.snackbar.Snackbar

class ResultsSearchActivity : AppCompatActivity() {

    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultAdapter: HomeAdapter
    private lateinit var results: Array<Ad>
    private lateinit var sortItem: Button
    private lateinit var filterItem: Button
    private lateinit var noResults: TextView
    private var userID = 0
    private var category = ""
    private var voivodeship = ""
    private var sort = 0
    private var filterS = ""
    private var filterC = ""
    private var filterR = ""
    private var query = ""
    private lateinit var dbHelper : DataBaseHelper

    private val networkConnection = NetworkConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_search)

        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            category = extras.getString("category").toString()
            voivodeship = extras.getString("voivodeship").toString()
            sort = extras.getInt("sort")
            filterS = extras.getString("filterS").toString()
            filterR = extras.getString("filterR").toString()
            filterC = extras.getString("filterC").toString()
            query = extras.getString("query").toString()
        }

        dbHelper = DataBaseHelper(this)

        noResults = findViewById(R.id.nothingFound)

        sortItem = findViewById(R.id.sortButton)
        filterItem = findViewById(R.id.filterButton)
        sortItem.setOnClickListener{
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, SortActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "query" to query,
                        "userID" to userID,
                        "category" to category,
                        "voivodeship" to voivodeship,
                        "sort" to sort,
                        "filter" to hashMapOf("R" to filterR, "S" to filterS, "C" to filterC)
                    )
                )
                startActivity(intent)
            }
        }

        filterItem.setOnClickListener{
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, FilterActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "query" to query,
                        "userID" to userID,
                        "category" to category,
                        "voivodeship" to voivodeship,
                        "filter" to hashMapOf("R" to filterR, "S" to filterS, "C" to filterC),
                        "sort" to sort
                    )
                )
                startActivity(intent)
            }
        }

        resultRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultRecyclerView.layoutManager = GridLayoutManager(this, 3)
        resultAdapter = HomeAdapter(emptyArray(), this)
        resultRecyclerView.adapter = resultAdapter

        if(category != "") {
            title = "Kategoria: $category"
            if(sort != 0) {
                results = getCategorySortResult(filterS, filterR, sort)
            } else {
                results = getCategoryResult(filterS, filterR)
            }
            if(results.size == 0){
                noResults.visibility = View.VISIBLE
            } else {
                noResults.visibility = View.GONE
            }
            resultAdapter.dataset = results
            resultAdapter.notifyDataSetChanged()

        }
        if(voivodeship != "") {
            title = "Województwo: $voivodeship"
            if(sort != 0) {
                title = "Województwo: $voivodeship"
                results = getVoivodeshipSortResult(filterS, filterR, filterC, sort)
            } else {
                title = "Województwo: $voivodeship"
                results = getVoivodeshipResult(filterS, filterR, filterC)
            }
            resultAdapter.dataset = results
            resultAdapter.notifyDataSetChanged()
            if(results.isEmpty()){
                noResults.visibility = View.VISIBLE
            } else {
                noResults.visibility = View.GONE
            }
        }

        println("CAT: $category, VOI: $voivodeship")
        if(category == "" && voivodeship == ""){
            title = "Wyniki wyszukiwania"
            results = if(sort != 0) {
                getSortResult(query, filterS, filterR, filterC, sort)
            } else {
                getResult(query, filterS, filterR, filterC)
            }
            if(results.size == 0){
                noResults.visibility = View.VISIBLE
            } else {
                noResults.visibility = View.GONE
            }
            resultAdapter.dataset = results
            resultAdapter.notifyDataSetChanged()
        }

        val context = this

        resultAdapter.setOnClickListener(object : HomeAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                if (!networkConnection.isNetworkAvailable(applicationContext)) {
                    Snackbar.make(
                        findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(context, AdDetailsActivity::class.java)
                    intent.putExtras(
                        bundleOf(
                            "userID" to userID,
                            "profileID" to results[pos].user,
                            "adID" to results[pos].ID
                        )
                    )
                    startActivity(intent)
                }
            }
        })
    }

    override fun getParentActivityIntent(): Intent? {
        var i: Intent? = null
        if(query != "null"){
            i = Intent(this, ClueWordSearchActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            i.putExtras( bundleOf("userID" to userID, "query" to query))
        } else {
            i = Intent(this, MainActivity::class.java)
            i.putExtras(bundleOf("userID" to userID, "adID" to -1))
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return i
    }



    private fun getResult(text: String, filterS: String, filterR: String, filterC: String): Array<Ad> {
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByStatus(text, userID, filterS)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByRate(text, userID, filterR)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsCategory(text, userID, filterC)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAds(text, userID)
        }
        return result
    }

    private fun getSortResult(text: String, filterS: String, filterR: String, filterC: String, sort: Int): Array<Ad> {
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByStatus(text, userID, filterS, sort)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS, sort)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR, sort)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByRate(text, userID, filterR, sort)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsCategory(text, userID, filterC, sort)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS, sort)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC, sort)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR, sort)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC, sort)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByStatus(text, userID, filterS, sort)
            val rateResult = dbHelper.findAdsByRate(text, userID, filterR, sort)
            val categoryResult = dbHelper.findAdsCategory(text, userID, filterC, sort)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAds(text, userID, sort)
        }
        return result
    }

    private fun getCategorySortResult(filterS: String, filterR: String, sort: Int) : Array<Ad> {
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null") {
            dbHelper.findAdsByCategoryByStatus(category, userID, sort, filterS)
        } else if(filterS != "null" && filterR != "null"){
            val statusResult = dbHelper.findAdsByCategoryByStatus(category, userID, sort, filterS)
            val rateResult = dbHelper.findAdsByCategoryByRate(category, userID, sort, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null"){
            dbHelper.findAdsByCategoryByRate(category, userID, sort, filterR)
        }  else {
            dbHelper.findAdsByCategory(category, userID, sort)
        }
        return result
    }

    private fun getCategoryResult(filterS: String, filterR: String) : Array<Ad> {
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null") {
            dbHelper.findAdsByCategoryByStatus(category, userID, filterS)
        } else if(filterS != "null" && filterR != "null"){
            val statusResult = dbHelper.findAdsByCategoryByStatus(category, userID, filterS)
            val rateResult = dbHelper.findAdsByCategoryByRate(category, userID, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null"){
            dbHelper.findAdsByCategoryByRate(category, userID, filterR)
        }  else {
            dbHelper.findAdsByCategory(category, userID)
        }
        return result
    }

    private fun getVoivodeshipSortResult(filterS: String, filterR: String, filterC: String, sort: Int) : Array<Ad> {
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, sort, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, sort, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, sort, filterC)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAdsByVoivodeship(voivodeship, userID, sort)
        }
        return result
    }

    private fun getVoivodeshipResult(filterS: String, filterR: String, filterC: String) : Array<Ad> {
        println("Status: $filterS, Rate: $filterR")
        val result = if(filterS != "null" && filterR == "null" && filterC == "null") {
            dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
        } else if(filterS != "null" && filterR != "null" && filterC == "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC == "null"){
            dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
        } else if(filterS == "null" && filterR == "null" && filterC != "null"){
            dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
        } else if(filterS != "null" && filterR == "null" && filterC != "null"){
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(c in categoryResult){
                    if(s.ID == c.ID){
                        tmp.add(s)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS == "null" && filterR != "null" && filterC != "null"){
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
            val tmp = mutableListOf<Ad>()
            for(r in rateResult){
                for(c in categoryResult){
                    if(c.ID == r.ID){
                        tmp.add(c)
                    }
                }
            }
            tmp.toTypedArray()
        } else if(filterS != "null" && filterR != "null" && filterC != "null") {
            val statusResult = dbHelper.findAdsByVoivodeshipByStatus(voivodeship, userID, filterS)
            val rateResult = dbHelper.findAdsByVoivodeshipByRate(voivodeship, userID, filterR)
            val categoryResult = dbHelper.findAdsByVoivodeshipByCategory(voivodeship, userID, filterC)
            val firstResult = mutableListOf<Ad>()
            val tmp = mutableListOf<Ad>()
            for(s in statusResult){
                for(r in rateResult){
                    if(s.ID == r.ID){
                        firstResult.add(s)
                    }
                }
            }
            for(f in firstResult){
                for(c in categoryResult){
                    if(f.ID == c.ID){
                        tmp.add(f)
                    }
                }
            }
            tmp.toTypedArray()
        } else {
            dbHelper.findAdsByVoivodeship(voivodeship, userID)
        }
        return result
    }

}