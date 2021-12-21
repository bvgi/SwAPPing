package com.example.swapping.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.MainActivity
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.example.swapping.ui.AdDetails.AdDetailsActivity
import com.example.swapping.ui.AdDetails.AdDetailsFragment
import com.example.swapping.ui.AdDetails.AdDetailsFragmentDirections
import com.example.swapping.ui.profile.ProfileFragmentDirections
import java.util.*
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var adapter: HomeAdapter
    lateinit var homeRecycler: RecyclerView
    lateinit var ads: Array<Ad>

    val arguments: HomeFragmentArgs by navArgs()
    var userID = 0

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            userID = arguments.userID
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel()::class.java)


        val tmp = homeViewModel.getFollowersAnnouncements(userID, root.context)
        if(tmp.isEmpty()) {
            val adsList =
                homeViewModel.getAnnouncements(userID, root.context).toMutableList()
            val randomAds = mutableListOf<Ad>()
            for(ad in adsList){
                if(ad.archived == 1)
                    adsList.drop(adsList.indexOf(ad))
            }
            var max = 20
            if(adsList.size < 10)
                 max = adsList.size
            for(i in 1..max){
                val random = Random.nextInt(adsList.size)
                if(!randomAds.contains(adsList[random]))
                    randomAds.add(adsList[random])
            }
            ads = randomAds.toTypedArray()
        }
        else
            ads = tmp


        for(ad in ads){
            if (ad.archived == 1){
                ads.drop(ads.indexOf(ad))
            }
        }


        adapter = HomeAdapter(arrayOf(), root.context)
        println("HOME::: Num of ads: ${ads.size}, userID: $userID")
        homeRecycler = root.findViewById(R.id.RecyclerViewHome)
        homeRecycler.layoutManager = GridLayoutManager(root.context, 3)
        homeRecycler.adapter = adapter
        adapter.dataset = ads
        adapter.notifyDataSetChanged()

        return root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)

        adapter.setOnClickListener(object : HomeAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                val action = HomeFragmentDirections.actionNavigationHomeToAdDetails() // TODO: czy to nie powinno być activity
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
