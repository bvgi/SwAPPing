package com.example.swapping.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R

class HomeAdapter (var dataset: Array<Ad>, val context: Context) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.announcement_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val userDB = DataBaseHelper(holder.itemView.context)
        val row = dataset[position]
        println(row.image)
        if(row.image.isNotEmpty()) {
            holder.image.setImageBitmap(getImage(row.image))
        } else {
            holder.image.setImageResource(R.drawable.no_photo_foreground)
        }
        holder.title.text = row.title
        holder.username.text = userDB.getUserById(row.user).username
        if(row.archived == 1){
            holder.title.text = holder.title.text.toString() + " [ZAKOŃCZONO]"
            holder.title.setTextColor(Color.GRAY)
            holder.username.setTextColor(Color.GRAY)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size

    lateinit var mClickListener: ClickListener

    fun getImage(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun setOnClickListener(aClickListener: ClickListener){
        mClickListener = aClickListener
    }

    interface ClickListener {
        fun onClick(pos: Int, aView: View)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        val image: ImageView
        val title: TextView
        val username: TextView

        init {
            view.setOnClickListener(this)
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.homeImage)
            title = view.findViewById(R.id.homeTitle)
            username = view.findViewById(R.id.homeUsername)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }






}