package com.example.swapping.ui.searching

import ClueWordTabAdapter
import SearchingTabAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.home.HomeAdapter
import com.google.android.material.tabs.TabLayout

class ClueWordSearchActivity : AppCompatActivity() {
//    private lateinit var clueWordTabLayout: TabLayout
//    private lateinit var clueWordViewPager: ViewPager
    private lateinit var clueWordSearchView: SearchView
    private lateinit var resultRecyclerView: RecyclerView
    private lateinit var resultAdapter: HomeAdapter
    private lateinit var results: Array<Ad>

    private val arguments: ClueWordSearchActivityArgs by navArgs()
    private var userID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clue_word_search)
        arguments.let {
            userID = it.userID
        }

        val dbHelper = DataBaseHelper(this)

        resultRecyclerView = findViewById(R.id.resultList)
        resultRecyclerView.layoutManager = GridLayoutManager(this, 3)
        resultAdapter = HomeAdapter(emptyArray(), this)
        resultRecyclerView.adapter = resultAdapter


//        clueWordTabLayout = findViewById(R.id.clueWordTabLayout)
//        clueWordViewPager = findViewById(R.id.clueWordPager)
//
//        val adapter = ClueWordTabAdapter(supportFragmentManager)
//        adapter.addFragment(Ads(), "Ogłoszenia")
//        adapter.addFragment(Users(), "Użytkownicy")
//        clueWordViewPager.adapter = adapter
//        clueWordTabLayout.setupWithViewPager(clueWordViewPager)
////        clueWordViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(clueWordTabLayout))
//        clueWordTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                clueWordViewPager.currentItem = tab.position
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })


//        println("CLUEWORD::: current tab : ${clueWordViewPager.currentItem}")

        clueWordSearchView = findViewById(R.id.clueWord)
        clueWordSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(p0 != null && p0 != "") {
                    results = dbHelper.findAds(p0, userID)
                    results.iterator().forEach { ad: Ad -> println(ad.title) }
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
                } else {
                    results = emptyArray()
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0 != null && p0 != ""){
                    results = dbHelper.findAds(p0, userID)
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
                } else {
                    results = emptyArray()
                    resultAdapter.dataset = results
                    resultAdapter.notifyDataSetChanged()
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