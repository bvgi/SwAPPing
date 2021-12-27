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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.example.swapping.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {
    private var userID = 0
    private var previousFragment = " "
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private lateinit var logOutButton: FloatingActionButton
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var editTextView: TextView
    private lateinit var profileViewLayout: LinearLayout
    private lateinit var userAds: LinearLayout
    private lateinit var userFavs: LinearLayout

    private val networkConnection = NetworkConnection()

    private val args: ProfileFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        userID = args.userID
        previousFragment = args.previousFragment

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = DataBaseHelper(root.context)

        if(previousFragment == "adDetails"){
            val homeFragment = HomeFragment()
            homeFragment.setArguments(bundleOf("userID" to userID, "previousFragment" to "Profile"))
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, homeFragment)?.commit()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAds = view.findViewById(R.id.userAds)
        userAds.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action = ProfileFragmentDirections.actionNavigationProfileToUserAdsFragment()
                action.previousFragment = "Profile"
                action.userID = userID
                action.profileID = userID
                findNavController().navigate(action)
            }
        }


        logOutButton = view.findViewById(R.id.logoutButton)
        logOutButton.setOnClickListener {
            dbHelper.setLoggedOut(userID)
            val action = ProfileFragmentDirections.actionNavigationProfileToLoginActivity().setUserID(userID)
            view.findNavController().navigate(action)
        }

        userFavs = view.findViewById(R.id.userFavs)
        userFavs.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action = ProfileFragmentDirections.actionNavigationProfileToUserAdsFragment()
                action.previousFragment = "Liked"
                action.userID = userID
                action.profileID = userID
                findNavController().navigate(action)
            }
        }

        editTextView = view.findViewById(R.id.editProfile)
        editTextView.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action =
                    ProfileFragmentDirections.actionNavigationProfileToEditProfileActivity()
                        .setUserID(userID)
                view.findNavController().navigate(action)
            }
        }

        profileViewLayout = view.findViewById(R.id.profileViewLayout)
        profileViewLayout.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action =
                    ProfileFragmentDirections.actionNavigationProfileToProfileViewFragment()
                action.userID = userID
                action.profileID = userID
                view.findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}