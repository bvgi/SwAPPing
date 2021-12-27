package com.example.swapping.ui.notifications

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.R

class NotificationAdapter(var dataSet: Array<Triple<Pair<Int, String>, Pair<Int, String>, Int>>, val context: Context) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    lateinit var mClickListener: ReviewsClickListener
    lateinit var deleteButton: Button

    interface ReviewsClickListener {
        fun onClick(pos: Int, aView: View)
    }

    fun setOnClickListener(aClickListener: ReviewsClickListener){
        mClickListener = aClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val title: TextView
        val username: TextView
        val state: ImageView

        init {
            view.setOnClickListener(this)
            username = view.findViewById(R.id.notificationUsername)
            title = view.findViewById(R.id.notificationAdTitle)
            state = view.findViewById(R.id.notificatioType)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.notification_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataSet[position]
        viewHolder.title.text = data.first.second
        viewHolder.username.text = "@" + data.second.second
        when(data.third){
            4 -> viewHolder.state.setImageResource(R.drawable.ic_baseline_stop_circle_24)              // odrzucone
            1 -> viewHolder.state.setImageResource(R.drawable.ic_baseline_new_releases_24)              // rozpoczęte
            2 -> {
                viewHolder.state.setImageResource(R.drawable.ic_baseline_check_circle_24)
                viewHolder.title.text = viewHolder.title.text.toString() + " [ZAKOŃCZONE]"
                viewHolder.title.setTextColor(Color.GRAY)
            }              // zaakceptowane
            3 -> viewHolder.state.setImageResource(R.drawable.ic_baseline_swap_horizontal_circle_24)    // kontynuacja
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}