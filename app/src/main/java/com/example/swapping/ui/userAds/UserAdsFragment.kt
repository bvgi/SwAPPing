package com.example.swapping.ui.userAds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.MainActivity
import com.example.swapping.Models.Ad
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.home.HomeViewModel

class UserAdsFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var adapter: HomeAdapter
    lateinit var homeRecycler: RecyclerView
    lateinit var ads: Array<Ad>
    private lateinit var noFav: TextView

    val arguments: UserAdsFragmentArgs by navArgs()
    var userID = 0
    var previous = ""

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            userID = arguments.userID
            previous = arguments.previousFragment
        }
        if (previous == "Liked" || previous == "Profile")
            setHasOptionsMenu(true)
        if (previous == "Liked")
            (activity as MainActivity?)?.supportActionBar?.title = "Polubione"
        if(previous == "Profile")
            (activity as MainActivity?)?.supportActionBar?.title = "Moje ogłoszenia"
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
        when(arguments.previousFragment){
            "Profile" -> ads = homeViewModel.getUserAnnouncements(userID, root.context) + homeViewModel.getPurchasedAnnouncements(userID, root.context)
            "Liked" -> {
                ads = homeViewModel.getUserLiked(userID, root.context)
                if(ads.isEmpty()) {
                    noFav = root.findViewById(R.id.noFav)
                    noFav.visibility = View.VISIBLE
                }
            }
         }

        if(arguments.previousFragment != "Profile"){
            for(ad in ads){
                if (ad.archived == 1){
                    ads.drop(ads.indexOf(ad))
                }
            }
        }

        adapter = HomeAdapter(arrayOf(), root.context)
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
                when(arguments.previousFragment){
                    "Profile" -> {
                        val action = UserAdsFragmentDirections.actionUserAdsFragmentToAdDetailsActivity()
                        action.adID = ads[pos].ID
                        action.profileID = userID
                        action.userID = userID
                        findNavController().navigate(action)
                    }
                    "Liked" -> {
                        val action = UserAdsFragmentDirections.actionUserAdsFragmentToAdDetailsFragment()
                        action.adID = ads[pos].ID
                        action.profileID = ads[pos].ID
                        action.userID = userID
                        action.previousFragment = "Liked"
                        findNavController().navigate(action)
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val action = UserAdsFragmentDirections.actionUserAdsFragmentToNavigationProfile()
                action.userID = userID
                findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
