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
import com.example.swapping.ui.profile.UsersListAdapter

class Categories : Fragment() {
    lateinit var adapter: SearchingListAdapter
    lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categories: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dbHelper = DataBaseHelper(requireContext())
        categories = dbHelper.getCategories()

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchingListAdapter(arrayOf(), view.context)
        categoriesRecyclerView = view.findViewById(R.id.listOfCategories)
        categoriesRecyclerView.layoutManager = LinearLayoutManager(view.context)
        categoriesRecyclerView.adapter = adapter
        categoriesRecyclerView.addItemDecoration(
            DividerItemDecoration(view.context,
                DividerItemDecoration.VERTICAL)
        )
        adapter.dataSet = categories
        adapter.notifyDataSetChanged()
    }
}