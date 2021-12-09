package com.example.swapping.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.Image
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Review
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.example.swapping.databinding.FragmentProfileViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import kotlin.math.round

// TODO: Dodawanie i usuwanie ocen

class ProfileViewFragment : Fragment() {

    var userID = 0
    var profileID = 0
    private lateinit var profileViewViewModel: ProfileViewViewModel
    private var _binding: FragmentProfileViewBinding? = null
    private lateinit var dbHelper: DataBaseHelper
    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var observeButton: Button
    private lateinit var addOpinionLayout: LinearLayout
    private lateinit var profileAds: TextView
    private lateinit var rateStars: Array<ImageView>
    private lateinit var rateDescription: EditText
    private lateinit var addReviewButton: Button
    private lateinit var meanRate: TextView
    private lateinit var alert: TextView
    private lateinit var reviewsContent: LinearLayout

    private lateinit var reviews: Array<Review>
    private lateinit var reviewsRecycler: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter

    var finalrate = 0

    private lateinit var root: View

    val arg: ProfileViewFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userID = arg.userID
//        profileID = arg.profileID
        profileID = 1
        profileViewViewModel =
            ViewModelProvider(this).get(ProfileViewViewModel::class.java)

        _binding = FragmentProfileViewBinding.inflate(inflater, container, false)

        root = binding.root


        reviewsContent = root.findViewById(R.id.reviewsContents)
        reviews = profileViewViewModel.getReviews(profileID, root.context)
        println(reviews)

        if(reviews.size == 0)
            reviewsContent.visibility = View.GONE



//        reviewsAdapter.button?.setOnClickListener {
//            dbHelper.deleteReview(userID, profileID)
//            var reviewIndex = 0
//            for(review in reviews){
//                if(review.reviewer == profileID){
//                    reviewIndex = reviews.indexOf(review)
//                }
//            }
//            reviews.drop(reviewIndex)
//            println(reviews.size)
//            reviewsAdapter.notifyItemRemoved(reviewIndex)
//            reviewsAdapter.notifyItemRangeChanged(reviewIndex, reviews.size)
//        }
        var reviewIndex = 0
//        val button = inflater.inflate(R.layout.review_row, container, false).findViewById<ImageButton>(R.id.deleteReview)
//        button.setOnClickListener {
//            dbHelper.deleteReview(userID, profileID)
//
//            for(review in reviews){
//                if(review.reviewer == profileID){
//                    reviewIndex = reviews.indexOf(review)
//                }
//            }
//            reviews.drop(reviewIndex)
//            println(reviews.size)
//            reviewsAdapter.notifyItemRemoved(reviewIndex)
//            reviewsAdapter.notifyItemRangeChanged(reviewIndex, reviews.size)
//        }

        for(review in reviews){
            if(review.reviewer == profileID){
                reviewIndex = reviews.indexOf(review)
            }
        }

        println(reviewIndex)

        observeButton = root.findViewById(R.id.observeButton)
        addOpinionLayout = root.findViewById(R.id.addOpinionLayout)
        profileAds = root.findViewById(R.id.profileAds)
        rateDescription = root.findViewById(R.id.reviewContent)


        if (profileID == userID) {
            observeButton.visibility = View.GONE
            addOpinionLayout.visibility = View.GONE
            profileAds.visibility = View.GONE
        }


        if(profileViewViewModel.isReviewer(userID, profileID, root.context))
            addOpinionLayout.visibility = View.GONE

        return root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DataBaseHelper(view.context)

        reviewsRecycler = view.findViewById(R.id.reviews)
        reviewsRecycler.layoutManager = LinearLayoutManager(view.context)
        reviewsAdapter = ReviewsAdapter(Pair(userID, reviews), view.context)
        reviewsRecycler.isNestedScrollingEnabled = false
        reviewsRecycler.adapter = reviewsAdapter

        val userData = dbHelper.getUserById(profileID)

        println("PF: ${userData.toString()}")

        username = view.findViewById(R.id.profileUsername)
        username.text = userData.username

        email = view.findViewById(R.id.profileEmail)
        email.text = email.text.toString() + userData.email

        phoneNumber = view.findViewById(R.id.profilePhoneNumber)
        phoneNumber.text = phoneNumber.text.toString() + userData.phone_number

        observeButton.setOnClickListener {
            if(observeButton.text == resources.getString(R.string.follow)){
                observeButton.text = "Przestań obserwować"
//                observeButton.setBackgroundColor(Color.GRAY)
            }
            else{
                observeButton.text = resources.getString(R.string.follow)
//                observeButton.setBackgroundColor(ContextCompat.getColor(view.context, R.color.light_green))
            }
        }


        val star1 = view.findViewById<ImageView>(R.id.star1)
        val star2 = view.findViewById<ImageView>(R.id.star2)
        val star3 = view.findViewById<ImageView>(R.id.star3)
        val star4 = view.findViewById<ImageView>(R.id.star4)
        val star5 = view.findViewById<ImageView>(R.id.star5)
        rateStars = arrayOf(star1, star2, star3, star4, star5)


        star1.setOnClickListener {
            profileViewViewModel.changeStars(rateStars, 1)
            finalrate = 1
        }

        star2.setOnClickListener {
            profileViewViewModel.changeStars(rateStars, 2)
            finalrate = 2
        }

        star3.setOnClickListener {
            profileViewViewModel.changeStars(rateStars, 3)
            finalrate = 3
        }

        star4.setOnClickListener {
            profileViewViewModel.changeStars(rateStars, 4)
            finalrate = 4
        }

        star5.setOnClickListener {
            profileViewViewModel.changeStars(rateStars, 5)
            finalrate = 5
        }

        val countedMeanRate = profileViewViewModel.getMeanRate(reviews)
        meanRate = view.findViewById(R.id.meanRateValue)
        if (reviews.isNotEmpty())
            meanRate.text = countedMeanRate.toString()
        
        println("Review: $profileID, $userID, $finalrate, ${rateDescription.text}")

        alert = view.findViewById(R.id.rateAlert)

        addReviewButton = view.findViewById(R.id.saveReviewButton)
        addReviewButton.setOnClickListener {
            if(finalrate != 0) {
                val description = rateDescription.text.toString()

                val review = Review(
                    user = profileID,
                    reviewer = userID,
                    rate = finalrate,
                    description = description
                )
                dbHelper.addReview(review)
                reviews = mutableListOf(review).toTypedArray() + reviews
                println("Size: " + reviews.size)
                reviewsAdapter.notifyItemInserted(0)
                reviewsAdapter.notifyItemRangeChanged(0, reviews.size)
                reviewsRecycler.adapter = reviewsAdapter
                meanRate.text = round(profileViewViewModel.getMeanRate(reviews) * 10.0 / 10.0).toString()
                Snackbar.make(
                    view.findViewById(R.id.reviewInformation),
                    "Ocena została dodana",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                alert.visibility = View.VISIBLE
            }
        }


    }



}