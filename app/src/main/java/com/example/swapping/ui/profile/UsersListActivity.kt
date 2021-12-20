package com.example.swapping.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R
import com.example.swapping.databinding.FragmentUsersListBinding

class UsersListActivity : AppCompatActivity() {
    private var userID: Int = 0
    private var listType: Int = 0
    private var profileID: Int = 0
    private var prev: String = ""

    lateinit var adapter: UsersListAdapter
    lateinit var userListRecycler: RecyclerView
    lateinit var users: Array<Triple<Int, String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            profileID = extras.getInt("profileID")
            listType = extras.getInt("type")
        }

        val dbHelper = DataBaseHelper(this)

        if(listType == 0) { // 0 - followers, 1 - following
            users = dbHelper.getFollowers(profileID)
            println("FOLLOWERS LIST::: ${users.size}")
        } else {
            users = dbHelper.getFollowing(profileID)
            println("FOLLOWING LIST::: ${users.size}")
        }

        adapter = UsersListAdapter(users, this)
        userListRecycler = findViewById(R.id.UsersList)
        userListRecycler.layoutManager = LinearLayoutManager(this)
        userListRecycler.adapter = adapter
        userListRecycler.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )
        val context = this

        adapter.setOnClickListener(object : UsersListAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                val intent = Intent(context, ProfileViewActivity::class.java)
                intent.putExtras(bundleOf("userID" to userID, "profileID" to profileID))
                startActivity(intent)
            }
        })

    }
}