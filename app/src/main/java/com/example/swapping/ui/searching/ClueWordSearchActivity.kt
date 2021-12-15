package com.example.swapping.ui.searching

import SearchingTabAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.swapping.R
import com.google.android.material.tabs.TabLayout

class ClueWordSearchActivity : AppCompatActivity() {
    private lateinit var clueWordTabLayout: TabLayout
    private lateinit var clureWordViewPager: ViewPager
    private lateinit var clueWordSearchView: SearchView

    private val arguments: ClueWordSearchActivityArgs by navArgs()
    private var userID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clue_word_search)
        arguments.let {
            userID = it.userID
        }

        clueWordTabLayout = findViewById(R.id.clueWordTabLayout)
        clueWordTabLayout.addTab(clueWordTabLayout.newTab().setText("Ogłoszenia"))
        clueWordTabLayout.addTab(clueWordTabLayout.newTab().setText("Użytkownicy"))

        clureWordViewPager = findViewById(R.id.clueWordPager)

        val adapter = SearchingTabAdapter(this, supportFragmentManager, clueWordTabLayout.tabCount)
        clureWordViewPager.adapter = adapter
        clureWordViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(clueWordTabLayout))
        clueWordTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                clureWordViewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}