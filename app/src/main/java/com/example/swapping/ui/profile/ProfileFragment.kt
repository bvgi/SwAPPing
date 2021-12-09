package com.example.swapping.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.example.swapping.ui.userLogin.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileFragment : Fragment() {
    var index = 0
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    lateinit var logOutButton: FloatingActionButton
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var editTextView: TextView
    private lateinit var profileViewLayout: LinearLayout

    private val args: ProfileFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        index = args.userID

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        val root: View = inflater.inflate(R.layout.fragment_profile, container, false)

        dbHelper = DataBaseHelper(root.context)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(index)

        logOutButton = view.findViewById(R.id.logoutButton)
        logOutButton.setOnClickListener {
            dbHelper.setLoggedOut(index)
            val action = ProfileFragmentDirections.actionNavigationProfileToLoginActivity().setUserID(index)
            view.findNavController().navigate(action)
//            val loginIntent = Intent(view.context, LoginActivity::class.java)
//            loginIntent.putExtra("userid", index)
//            startActivityForResult(loginIntent, 2)
        }

        editTextView = view.findViewById(R.id.editProfile)
        editTextView.setOnClickListener {
//            val editIntent = Intent(activity, EditProfileActivity::class.java)
//            editIntent.putExtra("userid", index)
//            startActivity(editIntent)
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