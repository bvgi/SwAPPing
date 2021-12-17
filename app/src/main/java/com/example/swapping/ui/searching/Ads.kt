package com.example.swapping.ui.searching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R

class Ads : Fragment() {

    lateinit var adapter: ArrayAdapter<String>
    lateinit var adsRecyclerView: ListView
    private lateinit var ads: Array<Pair<Int, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_localizations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, emptyArray())
        adsRecyclerView = view.findViewById(R.id.listOfVoivodeships)
//        adsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        adsRecyclerView.adapter = adapter
//        adsRecyclerView.addItemDecoration(
//            DividerItemDecoration(view.context,
//                DividerItemDecoration.VERTICAL)
//        )
    }

//    fun findAds(clueWord: String){
//        val dbHelper = DataBaseHelper(requireContext())
//        ads = dbHelper.findAds(clueWord)
//        adapter. = ads
//        adapter.notifyDataSetChanged()
//    }

}
