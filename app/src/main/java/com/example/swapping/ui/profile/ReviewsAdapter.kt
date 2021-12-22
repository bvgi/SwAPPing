package com.example.swapping.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Review
import com.example.swapping.R
import com.example.swapping.ui.home.HomeAdapter

class ReviewsAdapter(var dataSet: Pair<Int, Array<Review>>, val context: Context) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>(){
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    lateinit var mClickListener: ReviewsClickListener

    interface ReviewsClickListener {
        fun onClick(pos: Int, aView: View)
    }

    fun setOnClickListener(aClickListener: ReviewsClickListener){
        mClickListener = aClickListener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val username: TextView
        val deleteReview: ImageView
        val description: TextView
        val star1: ImageView
        val star2: ImageView
        val star3: ImageView
        val star4: ImageView
        val star5: ImageView

        init {
            username = view.findViewById(R.id.reviewerName)
            deleteReview = view.findViewById(R.id.deleteReview)
            description = view.findViewById(R.id.rateDescription)
            star1 = view.findViewById(R.id.reviewStar1)
            star2 = view.findViewById(R.id.reviewStar2)
            star3 = view.findViewById(R.id.reviewStar3)
            star4 = view.findViewById(R.id.reviewStar4)
            star5 = view.findViewById(R.id.reviewStar5)
            deleteReview.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick(adapterPosition, itemView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.review_row, viewGroup, false)

        return ViewHolder(view)
    }



    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val review = dataSet.second[position]
        val dbHelper = DataBaseHelper(context)
        val userID = review.user

        val reviewer = dbHelper.getUserById(review.reviewer)
        val reviewerID = reviewer.ID
        val reviewerUsername = reviewer.username
        val stars = arrayOf(viewHolder.star1, viewHolder.star2, viewHolder.star3, viewHolder.star4, viewHolder.star5)
        if(dataSet.first == reviewerID)
            viewHolder.deleteReview.visibility = View.VISIBLE
        else
            viewHolder.deleteReview.visibility = View.GONE
        viewHolder.username.text = reviewerUsername
        viewHolder.description.text = review.description
        changeStars(stars, review.rate)

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.second.size

    fun changeStars(starsArray: Array<ImageView>, starNumber: Int){
        for(i in 0 until starNumber) {
            starsArray[i].setImageResource(R.drawable.ic_round_star_rate_24)
            starsArray[i].scaleX = 0.8F
            starsArray[i].scaleY = 0.8F
        }
    }



}