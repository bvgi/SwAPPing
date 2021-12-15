package com.example.swapping.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R
import com.example.swapping.databinding.FragmentNotificationsBinding

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

        val userAds = dbHelper.getUserAnnouncements(userID)
        val notificationAds = mutableListOf<Triple<Pair<Int, String>, Pair<Int, String>, Int>>()
        for(ad in userAds){
            if(ad.negotiation != 0) {
                val purchaser = dbHelper.getUserById(ad.purchaser_id).username
                notificationAds.add(
                    Triple(
                        Pair(ad.ID, ad.title),
                        Pair(ad.purchaser_id, purchaser),
                        ad.negotiation
                    )
                )
                println("NOTIFICATIONS::: Title ${ad.title}, User $purchaser, State ${ad.negotiation}")
            }
        }

        notificationAdapter = NotificationAdapter(notificationAds.toTypedArray(), root.context)
        notificationRecyclerView = root.findViewById(R.id.userNotifications)
        notificationRecyclerView.layoutManager = LinearLayoutManager(root.context)
        notificationRecyclerView.adapter = notificationAdapter
        notificationRecyclerView.addItemDecoration(
            DividerItemDecoration(root.context,
                DividerItemDecoration.VERTICAL)
        )

        // TODO: OnClick -> pokazać szczegóły, tj. tytuł, użytkownik, przedmioty oraz opcje


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}