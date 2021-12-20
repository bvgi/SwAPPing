package com.example.swapping.ui.searching

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.home.HomeAdapter

class ClueWordSearchActivity : AppCompatActivity() {
    private lateinit var clueWordSearchView: SearchView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultAdapter: HomeAdapter
    private lateinit var results: Array<Ad>

    private lateinit var sortItem: Button
    private lateinit var filterItem: Button
    private lateinit var noResults: TextView

    private val arguments: ClueWordSearchActivityArgs by navArgs()
    private var userID = 0
    private var sort = 0
    private var filterS = ""
    private var filterC = ""
    private var filterR = ""
    private var query = ""
    private lateinit var dbHelper : DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clue_word_search)
        arguments.let {
            userID = it.userID
        }
        val extras: Bundle? = intent.extras
        if(extras != null){
            sort = extras.getInt("sort")
            filterS = extras.getString("filterS").toString()
            filterR = extras.getString("filterR").toString()
            filterC = extras.getString("filterC").toString()
            query = extras.getString("query").toString()
        }

        dbHelper = DataBaseHelper(this)

        noResults = findViewById(R.id.nothingFound)

        resultRecyclerView = findViewById(R.id.resultList)
        resultRecyclerView.layoutManager = GridLayoutManager(this, 3)
        resultAdapter = HomeAdapter(emptyArray(), this)
        resultRecyclerView.adapter = resultAdapter

        clueWordSearchView = findViewById(R.id.clueWord)
        if(query != "null")
            clueWordSearchView.setQuery(query, true)
        val con = this
        clueWordSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(p0 != null && p0 != "") {
                    val intent = Intent(con, ResultsSearchActivity::class.java)
                    intent.putExtras(bundleOf("query" to p0, "userID" to userID, "category" to "", "voivodeship" to "", "filter" to hashMapOf("R" to filterR, "S" to filterS, "C" to filterC), "sort" to sort))
                    startActivity(intent)
                } else {
                    results = emptyArray()
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
                    if(results.isEmpty()){
                        noResults.visibility = View.VISIBLE
                    } else {
                        noResults.visibility = View.GONE
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0 != null && p0 != ""){
                    results = dbHelper.findAds(p0, userID)
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
                    if(results.isEmpty()){
                        noResults.visibility = View.VISIBLE
                    } else {
                        noResults.visibility = View.GONE
                    }
                } else {
                    results = emptyArray()
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
                    if(results.isEmpty()){
                        noResults.visibility = View.VISIBLE
                    } else {
                        noResults.visibility = View.GONE
                    }
                }
                return false
            }
        })
        val context = this

        resultAdapter.setOnClickListener(object : HomeAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                val intent = Intent(context, AdDetailsActivity::class.java)
                intent.putExtras( bundleOf("userID" to arguments.userID, "profileID" to results[pos].user, "adID" to results[pos].ID))
                startActivity(intent)
            }
            })
    }


    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtras(bundleOf("userID" to userID, "adID" to -1))
    }
}