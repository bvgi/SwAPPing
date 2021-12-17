package com.example.swapping.ui.searching
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.R

class Categories : Fragment() {
    lateinit var adapter: SearchingListAdapter
    lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categories: Array<String>
    var userID = 0

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
    ): View? {


        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbHelper = DataBaseHelper(view.context)
        categories = dbHelper.getCategories()
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

        adapter.setOnClickListener(object : SearchingListAdapter.ClickListener{
            override fun onClick(pos: Int, aView: View) {
                val intent = Intent(view.context, ResultsSearchActivity::class.java)
                intent.putExtras( bundleOf("userID" to userID, "category" to categories[pos],"voivodeship" to ""))
                startActivity(intent)
            }
            })
    }
}