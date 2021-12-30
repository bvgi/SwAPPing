package com.example.swapping.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.Models.Review
import com.example.swapping.R
import com.google.android.material.snackbar.Snackbar
import kotlin.math.round

class ProfileViewActivity : AppCompatActivity() {

    private var userID = 0
    private var profileID = 0
    private lateinit var profileViewViewModel: ProfileViewViewModel
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
    private lateinit var followers: TextView
    private lateinit var following: TextView
    private lateinit var reviewTitle: TextView
    private lateinit var profileName: TextView

    private lateinit var reviews: Array<Review>
    private lateinit var reviewsRecycler: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var reportUser: MenuItem

    private var finalrate = 0
    private val networkConnection = NetworkConnection()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)

        val extras: Bundle? = intent.extras
        if(extras != null){
            userID = extras.getInt("userID")
            profileID = extras.getInt("profileID")
        }

        profileViewViewModel =
            ViewModelProvider(this).get(ProfileViewViewModel::class.java)

        reviewsContent = findViewById(R.id.reviewsContents)
        reviews = profileViewViewModel.getReviews(profileID, this)
        println(reviews)
        reviewTitle = findViewById(R.id.reviewTitle)

        if(reviews.size == 0)
            reviewTitle.visibility = View.GONE

        var reviewIndex = 0

        for(review in reviews){
            if(review.reviewer == profileID){
                reviewIndex = reviews.indexOf(review)
            }
        }

        println(reviewIndex)

        observeButton = findViewById(R.id.observeButton)
        addOpinionLayout = findViewById(R.id.addOpinionLayout)
        profileAds = findViewById(R.id.profileAds)
        rateDescription = findViewById(R.id.reviewContent)


        if (profileID == userID) {
            observeButton.visibility = View.GONE
            addOpinionLayout.visibility = View.GONE
            profileAds.visibility = View.GONE
        }

        if(profileViewViewModel.isFollower(userID, profileID, this)){
            observeButton.text = "Przestań obserwować"
        }
        else{
            observeButton.text = resources.getString(R.string.follow)
        }


        if(profileViewViewModel.isReviewer(userID, profileID, this))
            addOpinionLayout.visibility = View.GONE


        reviewsRecycler = findViewById(R.id.reviews)
        reviewsRecycler.layoutManager = LinearLayoutManager(this)
        reviewsAdapter = ReviewsAdapter(Pair(userID, reviews), this)
        reviewsRecycler.isNestedScrollingEnabled = false
        reviewsRecycler.adapter = reviewsAdapter

        val context = this

        reviewsAdapter.setOnClickListener(object : ReviewsAdapter.ReviewsClickListener{
            override fun onClick(pos: Int, aView: View) {
                if (!networkConnection.isNetworkAvailable(applicationContext)) {
                    Snackbar.make(
                        findViewById(R.id.noInternet),
                        "Brak dostępu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    profileViewViewModel.deleteReview(profileID, userID, context)
                    reviews = profileViewViewModel.getReviews(profileID, context)
                    profileViewViewModel.updateMeanRate(
                        profileID,
                        round(profileViewViewModel.getMeanRate(reviews) * 10.0 / 10.0),
                        context
                    )
                    reviewsAdapter.notifyItemRemoved(pos)
                    reviewsAdapter.notifyItemRangeChanged(pos, reviews.size)
                }
            }
        })

        val userData = profileViewViewModel.getUser(profileID, this)

        profileName = findViewById(R.id.profileName)
        profileName.text = userData.name

        username = findViewById(R.id.profileUsername)
        username.text = "@" + userData.username

        email = findViewById(R.id.profileEmail)
        email.text = email.text.toString() + userData.email

        phoneNumber = findViewById(R.id.profilePhoneNumber)
        if(phoneNumber.text != "") {
            phoneNumber.visibility = View.VISIBLE
            phoneNumber.text = phoneNumber.text.toString() + userData.phone_number
        }

        followers = findViewById(R.id.followersNumber)
        following = findViewById(R.id.followingNumber)

        followers.text = profileViewViewModel.getFollowersSize(profileID, this).toString()
        followers.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, UsersListActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "userID" to userID,
                        "profileID" to profileID,
                        "followersOrFollowing" to 0
                    )
                )
                startActivity(intent)
            }
        }
        following.text = profileViewViewModel.getFollowingSize(profileID, this).toString()
        following.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, UsersListActivity::class.java)
                intent.putExtras(
                    bundleOf(
                        "userID" to userID,
                        "profileID" to profileID,
                        "followersOrFollowing" to 1
                    )
                )
                startActivity(intent)
            }
        }

        observeButton.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (!profileViewViewModel.isFollower(userID, profileID, context)) {
                    profileViewViewModel.follow(profileID, userID, this)
                    observeButton.text = "Przestań obserwować"
                    followers.text = profileViewViewModel.getFollowersSize(profileID, this).toString()
                } else {
                    profileViewViewModel.unfollow(profileID, userID, this)
                    observeButton.text = resources.getString(R.string.follow)
                    followers.text = profileViewViewModel.getFollowersSize(profileID, this).toString()
                }
            }
        }

        val star1 = findViewById<ImageView>(R.id.star1)
        val star2 = findViewById<ImageView>(R.id.star2)
        val star3 = findViewById<ImageView>(R.id.star3)
        val star4 = findViewById<ImageView>(R.id.star4)
        val star5 = findViewById<ImageView>(R.id.star5)
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
        meanRate = findViewById(R.id.meanRateValue)
        if (reviews.isNotEmpty())
            meanRate.text = countedMeanRate.toString()


        alert = findViewById(R.id.rateAlert)

        addReviewButton = findViewById(R.id.saveReviewButton)
        addReviewButton.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(applicationContext)) {
                Snackbar.make(
                    findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (finalrate != 0) {
                    val description = rateDescription.text.toString()

                    val review = Review(
                        user = profileID,
                        reviewer = userID,
                        rate = finalrate,
                        description = description
                    )
                    profileViewViewModel.addReview(review, this)
                    reviews = mutableListOf(review).toTypedArray() + reviews
                    reviewsAdapter.notifyItemInserted(0)
                    reviewsAdapter.notifyItemRangeChanged(0, reviews.size)
                    reviewsRecycler.adapter = reviewsAdapter
                    val meanRate = round(profileViewViewModel.getMeanRate(reviews) * 10.0 / 10.0)
                    profileViewViewModel.updateMeanRate(profileID, meanRate, this)
                    this.meanRate.text = meanRate.toString()
                } else {
                    alert.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        if(menu != null)
            reportUser = menu.findItem(R.id.menu_reportUser)
        if(profileID == userID)
            reportUser.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.menu_reportUser -> {
                Snackbar.make(
                    findViewById(R.id.reviewInformation),
                    "Zgłoszono użytkownika",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}