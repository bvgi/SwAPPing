package com.example.swapping.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.example.swapping.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ProfileFragment : Fragment() {
    var index = 0
    var previousFragment = " "
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    lateinit var logOutButton: FloatingActionButton
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var editTextView: TextView
    private lateinit var profileViewLayout: LinearLayout
    private lateinit var userAds: LinearLayout
    private lateinit var userFavs: LinearLayout

    private val args: ProfileFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        index = args.userID
        previousFragment = args.previousFragment


        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = DataBaseHelper(root.context)

        if(previousFragment == "AdDetails"){
            val homeFragment = HomeFragment()
            homeFragment.setArguments(bundleOf("userID" to index, "previousFragment" to "Profile"))
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, homeFragment)?.commit()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("PROFILE:::" + index)

        userAds = view.findViewById(R.id.userAds)
        userAds.setOnClickListener {
            val homeFragment = HomeFragment()
            homeFragment.setArguments(bundleOf("userID" to index, "previousFragment" to "Profile"))
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, homeFragment)?.commit()
        }


        logOutButton = view.findViewById(R.id.logoutButton)
        logOutButton.setOnClickListener {
            dbHelper.setLoggedOut(index)
            val action = ProfileFragmentDirections.actionNavigationProfileToLoginActivity().setUserID(index)
            view.findNavController().navigate(action)
//            val loginIntent = Intent(view.context, LoginActivity::class.java)
//            loginIntent.putExtra("userid", index)
//            startActivityForResult(loginIntent, 2)
        }

        userFavs = view.findViewById(R.id.userFavs)
        userFavs.setOnClickListener {
            val homeFragment = HomeFragment()
            homeFragment.setArguments(bundleOf("userID" to index, "previousFragment" to "Liked"))
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, homeFragment)?.commit()
        }

        editTextView = view.findViewById(R.id.editProfile)
        editTextView.setOnClickListener {
            val action = ProfileFragmentDirections.actionNavigationProfileToEditProfileActivity().setUserID(index)
            view.findNavController().navigate(action)
        }

        profileViewLayout = view.findViewById(R.id.profileViewLayout)
        profileViewLayout.setOnClickListener {
            val action = ProfileFragmentDirections.actionNavigationProfileToProfileViewFragment()
            action.userID = index
            action.profileID = index
            view.findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}