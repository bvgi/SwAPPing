package com.example.swapping.ui.userAds

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Ad
import com.example.swapping.R

class  UserAdsAdapter (var dataset: Array<Ad>, val context: Context) : RecyclerView.Adapter< UserAdsAdapter.ViewHolder>() {

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
        val checkBox: CheckBox

        init {
            // Define click listener for the ViewHolder's View.
            image = view.findViewById(R.id.adImage)
            title = view.findViewById(R.id.adTitle)
            username = view.findViewById(R.id.adUsername)
            checkBox = view.findViewById(R.id.adCheckBox)
            checkBox.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            mClickListener.onClick(adapterPosition, p0)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ad_check_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userDB = DataBaseHelper(holder.itemView.context)
        val row = dataset[position]
        if(row.image.isNotEmpty()) {
            holder.image.setImageBitmap(getImage(row.image))
        } else {
            holder.image.setImageResource(R.drawable.no_photo_foreground)
        }
        holder.title.text = row.title
        holder.username.text = userDB.getUserById(row.user).username
    }

    override fun getItemCount() = dataset.size

    lateinit var mClickListener: ClickListener

    fun getImage(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }








}