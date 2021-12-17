package com.example.swapping.ui.searching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.home.HomeAdapter

class ResultsSearchActivity : AppCompatActivity() {

    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultAdapter: HomeAdapter
    private lateinit var results: Array<Ad>
    private lateinit var toolbar: Toolbar
    private lateinit var sortItem: MenuItem
    private lateinit var filterItem: MenuItem
    private var userID = 0
    private var category = ""
    private var voivodeship = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_search)

        toolbar = findViewById(R.id.resultsToolbar)

        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            category = extras.getString("category").toString()
            voivodeship = extras.getString("voivodeship").toString()
        }

        val dbHelper = DataBaseHelper(this)



        resultRecyclerView = findViewById(R.id.resultsRecyclerView)
        resultRecyclerView.layoutManager = GridLayoutManager(this, 3)
        resultAdapter = HomeAdapter(emptyArray(), this)
        resultRecyclerView.adapter = resultAdapter

        if(category != "") {
            title = "Kategoria: $category"
            results = dbHelper.findAdsByCategory(category, userID)
        }
        if(voivodeship != "") {
            title = "Województwo: $voivodeship"
            results = dbHelper.findAdsByVoivodeship(voivodeship, userID)
        }
        resultAdapter.dataset = results
        resultAdapter.notifyDataSetChanged()

        val context = this

        resultAdapter.setOnClickListener(object : HomeAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                val intent = Intent(context, AdDetailsActivity::class.java)
                intent.putExtras( bundleOf("userID" to userID, "profileID" to results[pos].user, "adID" to results[pos].ID))
                startActivity(intent)
            }
        })
    }

    override fun getParentActivityIntent(): Intent? {
        return super.getParentActivityIntent()?.putExtras(bundleOf("userID" to userID, "adID" to -1))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)

        sortItem = menu?.findItem(R.id.menu_sortBy)!!
        filterItem = menu.findItem(R.id.menu_filter)

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_filter -> {
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        R.id.menu_sortBy -> {
            // User chose the "Favorite" action, mark the current item
            // as a favorite...
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}