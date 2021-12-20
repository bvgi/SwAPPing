package com.example.swapping.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R
import com.example.swapping.databinding.FragmentNotificationsBinding
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.home.HomeFragmentDirections

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null

    private var userID = 0

    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationArray: Array<Triple<String, String, Int>>

    private val binding get() = _binding!!
    private val arguments: NotificationsFragmentArgs by navArgs()

    private lateinit var dbHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            userID = it.userID
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = DataBaseHelper(root.context)

        val userNegotiations = dbHelper.getUserNegotiations(userID)
        val ownedAds = mutableListOf<Triple<Pair<Int, String>, Pair<Int, String>, Int>>()
        for(pair in userNegotiations){
            val ad = pair.first
            val negotiation = pair.second
        println(negotiation.ID)
            val purchaser = dbHelper.getUserById(negotiation.purchaserID).username
            ownedAds.add(
                Triple(
                    Pair(ad.ID, ad.title),
                    Pair(negotiation.purchaserID, purchaser),
                    negotiation.type
                )
            )
        }
//                println("NOTIFICATIONS::: Title ${ad.title}, User $purchaser, State ${ad.negotiation}")

        notificationAdapter = NotificationAdapter(ownedAds.toTypedArray(), root.context)
        notificationRecyclerView = root.findViewById(R.id.userNotifications)
        notificationRecyclerView.layoutManager = LinearLayoutManager(root.context)
        notificationRecyclerView.adapter = notificationAdapter
        notificationRecyclerView.addItemDecoration(
            DividerItemDecoration(root.context,
                DividerItemDecoration.VERTICAL)
        )

        notificationAdapter.setOnClickListener(object : NotificationAdapter.ReviewsClickListener{
            override fun onClick(pos: Int, aView: View) {
                val action = NotificationsFragmentDirections.actionNavigationNotificationsToNegotiationDetailsActivity()
                action.userID = arguments.userID
                action.negotiationID = userNegotiations[pos].second.ID
                root.findNavController().navigate(action)
            }
        }
        )

        // TODO: OnClick -> pokazać szczegóły, tj. tytuł, użytkownik, przedmioty oraz opcje


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}