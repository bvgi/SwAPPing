package com.example.swapping.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.example.swapping.ui.profile.ProfileFragmentDirections

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var adapter: HomeAdapter
    lateinit var homeRecycler: RecyclerView
    lateinit var ads: Array<Ad>

    val arguments: HomeFragmentArgs by navArgs()
    var userID = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel()::class.java)


        userID = arguments.userID
        ads = homeViewModel.getAnnouncements(userID, root.context)
        adapter = HomeAdapter(arrayOf(), root.context)
        println("HOME:::${ads.size}, $userID")
        homeRecycler = root.findViewById(R.id.RecyclerViewHome)
        homeRecycler.layoutManager = GridLayoutManager(root.context, 3)
        homeRecycler.adapter = adapter
        adapter.dataset = ads
        adapter.notifyDataSetChanged()

//
//        val title: TextView = homeRecycler.
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)

        adapter.setOnClickListener(object : HomeAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                val action = HomeFragmentDirections.actionNavigationHomeToAdUserNavigation()
                action.userID = arguments.userID
                action.profileID = ads[pos].user
                println(ads[pos].user)
                action.adID = ads[pos].ID
                root.findNavController().navigate(action)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
