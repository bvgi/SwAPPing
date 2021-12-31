package com.example.swapping.ui.searching

import SearchingTabAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.FragmentSearchingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class SearchingFragment : Fragment() {

    private var _binding: FragmentSearchingBinding? = null
    private val arguments: SearchingFragmentArgs by navArgs()
    private var userID = 0

    private val networkConnection = NetworkConnection()

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

        _binding = FragmentSearchingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clueWord: LinearLayout = binding.searchClueWord
        clueWord.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action =
                    SearchingFragmentDirections.actionNavigationSearchToClueWordSearchActivity()
                action.userID = userID
                view.findNavController().navigate(action)
            }
        }

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        val adapter = SearchingTabAdapter(childFragmentManager)
        val category = Categories()
        category.arguments = bundleOf("userID" to userID)
        val localizations = Localizations()
        localizations.arguments = bundleOf("userID" to userID)
        adapter.addFragment(category, "Kategoria")
        adapter.addFragment(localizations, "Lokalizacja")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}