package com.example.swapping.ui.newAd

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.swapping.R

import android.content.Intent
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swapping.databinding.FragmentNewAdBinding


class NewAdFragment : Fragment() {

    private val args: NewAdFragmentArgs by navArgs()

    private var _binding: FragmentNewAdBinding? = null

    private val binding get() = _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewAdBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val intent = Intent(activity, NewAdActivity::class.java)
//        intent.putExtra("userid", args.userID)
//        startActivity(intent)
//        val view: View = inflater.inflate(R.layout.fragment_new_ad, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = NewAdFragmentDirections.actionNavigationNewAnnouncementToNewAnnouncementActivity().setUserID(args.userID)

        view.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}