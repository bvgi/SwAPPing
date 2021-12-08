package com.example.swapping.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.example.swapping.ui.userLogin.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfileFragment : Fragment() {
    var index = 0
    private lateinit var profileViewModel: ProfileViewModel
//    private var _binding: FragmentProfileBinding? = null
    lateinit var logOutButton: FloatingActionButton
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var editTextView: TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val args = this.arguments
        if (args != null)
            index = args["userid"] as Int


        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)

//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root
        val root: View = inflater.inflate(R.layout.fragment_profile, container, false)

        dbHelper = DataBaseHelper(root.context)

        editTextView = root.findViewById(R.id.editProfile)
        editTextView.setOnClickListener {
            val editIntent = Intent(activity, EditProfileActivity::class.java)
            editIntent.putExtra("userid", index)
            startActivity(editIntent)
        }

        logOutButton = root.findViewById(R.id.logoutButton)
        logOutButton.setOnClickListener {
            dbHelper.setLoggedOut(index)
            val loginIntent = Intent(root.context, LoginActivity::class.java)
            loginIntent.putExtra("userid", index)
            startActivityForResult(loginIntent, 2)
        }

        return root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}