package com.example.swapping.ui.searching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R

class Users : Fragment() {

    lateinit var adapter: ClueWordSearchingAdapter
    lateinit var localizationsRecyclerView: RecyclerView
    private lateinit var users: Array<Pair<Int, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dbHelper = DataBaseHelper(requireContext())
        users = dbHelper.findUsers("test")


        return inflater.inflate(R.layout.fragment_localizations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ClueWordSearchingAdapter(emptyArray(), view.context)
        localizationsRecyclerView = view.findViewById(R.id.listOfVoivodeships)
        localizationsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        localizationsRecyclerView.adapter = adapter
        localizationsRecyclerView.addItemDecoration(
            DividerItemDecoration(view.context,
                DividerItemDecoration.VERTICAL)
        )
    }

}
