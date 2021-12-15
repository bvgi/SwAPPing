package com.example.swapping.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.Models.User
import com.example.swapping.R
import com.example.swapping.databinding.FragmentHomeBinding
import com.example.swapping.databinding.FragmentUsersListBinding
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.home.HomeViewModel

class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!
    private var userID: Int = 0
    private var listType: Int = 0
    private var profileID: Int = 0
    private var prev: String = ""
    private val arguments: UsersListFragmentArgs by navArgs()

    lateinit var adapter: UsersListAdapter
    lateinit var userListRecycler: RecyclerView
    lateinit var users: Array<Triple<Int, String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            profileID = it.profileID
            userID = it.userID
            listType = it.followersOrFollowing
            prev = it.previous
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val root: View = binding.root
        val dbHelper = DataBaseHelper(root.context)

        if(listType == 0) { // 0 - followers, 1 - following
            users = dbHelper.getFollowers(profileID)
            println("FOLLOWERS LIST::: ${users.size}")
        } else {
            users = dbHelper.getFollowing(profileID)
            println("FOLLOWING LIST::: ${users.size}")
        }

        adapter = UsersListAdapter(users, root.context)
        userListRecycler = root.findViewById(R.id.UsersList)
        userListRecycler.layoutManager = LinearLayoutManager(root.context)
        userListRecycler.adapter = adapter
        userListRecycler.addItemDecoration(DividerItemDecoration(root.context,DividerItemDecoration.VERTICAL))


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setOnClickListener(object : UsersListAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                if(prev == "Liked"){
                    val profileViewFragment = ProfileViewFragment()
                    profileViewFragment.arguments =
                        bundleOf("userID" to userID, "profileID" to profileID, "previous" to "Liked")
                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, profileViewFragment)?.commit()
                } else {
                    val action =
                        UsersListFragmentDirections.actionUsersListFragmentToNavigationProfileView()
                    action.profileID = users[pos].first
                    action.userID = userID
                    view.findNavController().navigate(action)
                }
            }
        })
    }

}