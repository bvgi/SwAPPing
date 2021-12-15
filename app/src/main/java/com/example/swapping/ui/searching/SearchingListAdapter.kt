package com.example.swapping.ui.searching

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.R

class SearchingListAdapter (var dataSet: Array<String>, val context: Context) : RecyclerView.Adapter<SearchingListAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    lateinit var mClickListener: ClickListener

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

    fun setOnClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val name: TextView

        init {
            view.setOnClickListener(this)
            name = view.findViewById(R.id.name)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.search_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val name = dataSet[position]
        viewHolder.name.text = name
    }

    override fun getItemCount() = dataSet.size

}