package com.example.swapping.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.Models.Announcement
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var adapter: HomeAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)
        adapter = HomeAdapter(arrayOf(Announcement(0, 0, "","","","","","",0,0,0,
            byteArrayOf(0),20210101)))
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val homeRecycler = view.findViewById<RecyclerView>(R.id.RecyclerViewHome)
        if (homeRecycler != null) {
            homeRecycler.layoutManager = GridLayoutManager(view.context!!, 3)
            homeRecycler.adapter = adapter
        }

        val root: View = binding.root
//
//        val title: TextView = homeRecycler.
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
