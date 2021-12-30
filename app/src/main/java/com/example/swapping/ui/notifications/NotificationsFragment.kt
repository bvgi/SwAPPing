package com.example.swapping.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.FragmentNotificationsBinding
import com.google.android.material.snackbar.Snackbar

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentNotificationsBinding? = null
    private val networkConnection = NetworkConnection()
    private var userID = 0

    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter

    private val binding get() = _binding!!
    private val arguments: NotificationsFragmentArgs by navArgs()


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

        val ownedAds = notificationsViewModel.getNegotiations(userID, root.context)

        notificationAdapter = NotificationAdapter(ownedAds.first, root.context)
        notificationRecyclerView = root.findViewById(R.id.userNotifications)
        notificationRecyclerView.layoutManager = LinearLayoutManager(root.context)
        notificationRecyclerView.adapter = notificationAdapter
        notificationRecyclerView.addItemDecoration(
            DividerItemDecoration(root.context,
                DividerItemDecoration.VERTICAL)
        )

        notificationAdapter.setOnClickListener(object : NotificationAdapter.ReviewsClickListener{
            override fun onClick(pos: Int, aView: View) {
                if (!networkConnection.isNetworkAvailable(root.context)) {
                    Snackbar.make(
                        root.findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val action =
                        NotificationsFragmentDirections.actionNavigationNotificationsToNegotiationDetailsActivity()
                    action.userID = arguments.userID
                    action.negotiationID = ownedAds.second[pos].second.ID
                    root.findNavController().navigate(action)
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}