package com.example.swapping.ui.searching

import SearchingTabAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.swapping.databinding.FragmentSearchingBinding
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class SearchingFragment : Fragment() {

    private lateinit var notificationsViewModel: SearchingViewModel
    private var _binding: FragmentSearchingBinding? = null
    val arguments: SearchingFragmentArgs by navArgs()
    var userID = 0


    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            userID = it.userID
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
                ViewModelProvider(this).get(SearchingViewModel::class.java)

        _binding = FragmentSearchingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val clueWord: LinearLayout = binding.searchClueWord
        clueWord.setOnClickListener {
            val action = SearchingFragmentDirections.actionNavigationSearchToClueWordSearchActivity()
            action.userID = userID
            root.findNavController().navigate(action)
        }

        val tabLayout: TabLayout = binding.tabLayout

        tabLayout.addTab(tabLayout.newTab().setText("Kategorie"))
        tabLayout.addTab(tabLayout.newTab().setText("Lokalizacja"))

        val viewPager: ViewPager = binding.viewPager

        val adapter = SearchingTabAdapter(root.context, this.requireFragmentManager(), tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}