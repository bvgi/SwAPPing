package com.example.swapping.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.UserDataBaseHelper
import com.example.swapping.Models.Announcement
import com.example.swapping.R
import com.example.swapping.ui.userLogin.LoginActivity

class HomeAdapter (val dataset: Array<Announcement>, val context: Context) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView
        val title: TextView
        val username: TextView

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.homeImage)
            title = view.findViewById(R.id.homeTitle)
            username = view.findViewById(R.id.homeUsername)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val userDB = UserDataBaseHelper(context)
        val row = dataset[position]
        val bmp = BitmapFactory.decodeByteArray(row.image, 0, row.image.size)
        holder.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, holder.image.width, holder.image.height, false))
        holder.title.text = row.title
        holder.username.text = userDB.getUserById(row.user).username
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size


}