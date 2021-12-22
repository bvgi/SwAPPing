package com.example.swapping.ui.searching

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.google.android.material.snackbar.Snackbar

class Localizations : Fragment() {
    lateinit var adapter: SearchingListAdapter
    lateinit var localizationsRecyclerView: RecyclerView
    private lateinit var localizations: Array<String>

    private val networkConnection = NetworkConnection()

    private var userID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extras: Bundle? = this.arguments
        if(extras != null){
            userID = extras.getInt("userID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_localizations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DataBaseHelper(view.context)
        localizations = dbHelper.getVoivodeships()

        adapter = SearchingListAdapter(emptyArray(), view.context)
        localizationsRecyclerView = view.findViewById(R.id.listOfVoivodeships)
        localizationsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        localizationsRecyclerView.adapter = adapter
        localizationsRecyclerView.addItemDecoration(
            DividerItemDecoration(view.context,
                DividerItemDecoration.VERTICAL)
        )
        adapter.dataSet = localizations
        adapter.notifyDataSetChanged()

        adapter.setOnClickListener(object : SearchingListAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                if (!networkConnection.isNetworkAvailable(view.context)) {
                    Snackbar.make(
                        view.findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(view.context, ResultsSearchActivity::class.java)
                    intent.putExtras(
                        bundleOf(
                            "userID" to userID,
                            "category" to "",
                            "voivodeship" to localizations[pos]
                        )
                    )
                    startActivity(intent)
                }
            }
        })

    }
}