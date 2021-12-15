package com.example.swapping.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.R
import com.example.swapping.ui.home.HomeAdapter

class UsersListAdapter (var dataSet: Array<Triple<Int, String, String>>, val context: Context) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>(){
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    lateinit var mClickListener: ClickListener

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

    fun setOnClickListener(aClickListener: ClickListener){
        mClickListener = aClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val userName: TextView
        val userUsername: TextView

        init {
            view.setOnClickListener(this)
            userName = view.findViewById(R.id.userName)
            userUsername = view.findViewById(R.id.userUsername)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val user = dataSet[position]
        viewHolder.userName.text = user.second
        viewHolder.userUsername.text = "@"+user.third
    }

    override fun getItemCount() = dataSet.size


}