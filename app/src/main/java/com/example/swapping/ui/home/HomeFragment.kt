package com.example.swapping.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.Models.Ad
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    lateinit var adapter: HomeAdapter
    lateinit var homeRecycler: RecyclerView
    lateinit var ads: Array<Ad>
    private val networkConnection = NetworkConnection()

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
                homeViewModel.getNotArchivedAds(userID, root.context).toMutableList()
            ads = adsList.shuffled().toTypedArray()
        }
        else
            ads = tmp


        for(ad in ads){
            if (ad.archived == 1){
                ads.drop(ads.indexOf(ad))
            }
        }
        ads = ads.toMutableList().shuffled().toTypedArray()
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
                if (!networkConnection.isNetworkAvailable(root.context)) {
                    Snackbar.make(
                        root.findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val action = HomeFragmentDirections.actionNavigationHomeToAdDetails()
                    action.userID = arguments.userID
                    action.profileID = ads[pos].user
                    println(ads[pos].user)
                    action.adID = ads[pos].ID
                    root.findNavController().navigate(action)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
