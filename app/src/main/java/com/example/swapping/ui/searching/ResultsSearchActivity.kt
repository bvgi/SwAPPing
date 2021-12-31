package com.example.swapping.ui.searching

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.Models.Ad
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.MainActivity
import com.example.swapping.ui.home.HomeAdapter
import com.google.android.material.snackbar.Snackbar

class ResultsSearchActivity : AppCompatActivity() {

    private lateinit var resultsSearchViewModel: ResultsSearchViewModel
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

        resultsSearchViewModel =
            ViewModelProvider(this).get(ResultsSearchViewModel::class.java)

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
                results = resultsSearchViewModel.getCategorySortResult(userID, category, filterS, filterR, sort, this)
            } else {
                results = resultsSearchViewModel.getCategoryResult(userID, category, filterS, filterR, this)
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
                results = resultsSearchViewModel.getVoivodeshipSortResult(userID, voivodeship, filterS, filterR, filterC, sort, this)
            } else {
                title = "Województwo: $voivodeship"
                results = resultsSearchViewModel.getVoivodeshipResult(userID, voivodeship, filterS, filterR, filterC, this)
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
                resultsSearchViewModel.getSortResult(userID, query, filterS, filterR, filterC, sort, this)
            } else {
                resultsSearchViewModel.getResult(userID, query, filterS, filterR, filterC, this)
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



}