package com.example.swapping.ui.newAnnouncement

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
import androidx.navigation.fragment.navArgs
import com.example.swapping.ui.profile.ProfileViewFragmentArgs


class NewAnnouncementFragment : Fragment() {

    val arg: NewAnnouncementFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val intent = Intent(activity, NewAnnouncementActivity::class.java)
        intent.putExtra("userid", arg.userID)
        startActivity(intent)

        val view: View = inflater.inflate(R.layout.fragment_new_announcement, container, false)

        return view
    }


}