package com.example.swapping.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.Image
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBase.DataBaseHelper
import com.example.swapping.Models.Review
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileBinding
import com.example.swapping.databinding.FragmentProfileViewBinding
import com.example.swapping.ui.AdDetails.AdDetailsFragment
import com.example.swapping.ui.home.HomeAdapter
import com.example.swapping.ui.home.HomeFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import kotlin.math.round

// TODO: Dodawanie i usuwanie ocen, przegląd followersów, napis na action bar

class ProfileViewFragment : Fragment() {

    var userID = 0
    var profileID = 0
    var prev = ""
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
    private lateinit var followers: TextView
    private lateinit var following: TextView
    private lateinit var reviewTitle: TextView
    private lateinit var profileName: TextView

    private lateinit var reviews: Array<Review>
    private lateinit var reviewsRecycler: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var reportUser: MenuItem

    var finalrate = 0

    private lateinit var root: View

    val arg: ProfileViewFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arg.let {
            profileID = it.profileID // ID profilu na który wchodzimy
            userID = it.userID // ID użytkownika korzystającego z aplikacji
            prev = it.previous // poprzedni fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        profileViewViewModel =
            ViewModelProvider(this).get(ProfileViewViewModel::class.java)
        _binding = FragmentProfileViewBinding.inflate(inflater, container, false)
        root = binding.root

        reviewsContent = root.findViewById(R.id.reviewsContents)
        reviews = profileViewViewModel.getReviews(profileID, root.context)
        println(reviews)
        reviewTitle = root.findViewById(R.id.reviewTitle)

        if(reviews.size == 0)
            reviewTitle.visibility = View.GONE

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

        if(profileViewViewModel.isFollower(userID, profileID, root.context)){
            observeButton.text = "Przestań obserwować"
//                observeButton.setBackgroundColor(Color.GRAY)
        }
        else{
            observeButton.text = resources.getString(R.string.follow)
//                observeButton.setBackgroundColor(ContextCompat.getColor(view.context, R.color.light_green))
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

        reviewsAdapter.setOnClickListener(object : ReviewsAdapter.ReviewsClickListener{
            override fun onClick(pos: Int, aView: View) {
                dbHelper.deleteReview(profileID, userID)
                reviews = profileViewViewModel.getReviews(profileID, root.context)
                dbHelper.updateMeanRate(profileID, round(profileViewViewModel.getMeanRate(reviews) * 10.0 / 10.0))
                reviewsAdapter.notifyItemRemoved(pos)
                reviewsAdapter.notifyItemRangeChanged(pos, reviews.size)
            }
        })

        val userData = dbHelper.getUserById(profileID)

        profileName = view.findViewById(R.id.profileName)
        profileName.text = userData.name

        username = view.findViewById(R.id.profileUsername)
        username.text = "@" + userData.username

        email = view.findViewById(R.id.profileEmail)
        email.text = email.text.toString() + userData.email

        phoneNumber = view.findViewById(R.id.profilePhoneNumber)
        if(phoneNumber.text != "") {
            phoneNumber.visibility = View.VISIBLE
            phoneNumber.text = phoneNumber.text.toString() + userData.phone_number
        }

        followers = view.findViewById(R.id.followersNumber)
        following = view.findViewById(R.id.followingNumber)

        followers.text = dbHelper.getFollowers(profileID).size.toString()
        followers.setOnClickListener {
            if(prev == "Liked"){
                val usersList = UsersListFragment()
                usersList.arguments =
                    bundleOf("userID" to userID, "profileID" to profileID, "followersOrFollowing" to 0, "previousFragment" to "Liked")
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, usersList)?.commit()
            } else {
                val action =
                    ProfileViewFragmentDirections.actionNavigationProfileViewToUsersListFragment()
                action.followersOrFollowing = 0
                action.profileID = profileID
                action.userID = userID
                findNavController().navigate(action)
            }
        }
        following.text = dbHelper.getFollowing(profileID).size.toString()
        following.setOnClickListener {
            if(prev == "Liked"){
                val usersList = UsersListFragment()
                usersList.arguments =
                    bundleOf("userID" to userID, "profileID" to profileID, "followersOrFollowing" to 1, "previous" to "Liked")
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment_activity_main, usersList)?.commit()
            } else {
                val action =
                    ProfileViewFragmentDirections.actionNavigationProfileViewToUsersListFragment()
                action.followersOrFollowing = 1
                action.profileID = profileID
                action.userID = userID
                findNavController().navigate(action)
            }
        }

        observeButton.setOnClickListener {
            if(!profileViewViewModel.isFollower(userID, profileID, view.context)){
                dbHelper.addFollower(profileID, userID)
                println("PROFILEVIEW::: Followers_size: ${dbHelper.getFollowers(profileID).size}")
                observeButton.text = "Przestań obserwować"
                followers.text = dbHelper.getFollowers(profileID).size.toString()

//                observeButton.setBackgroundColor(Color.GRAY)
            }
            else{
                dbHelper.deleteFollower(profileID, userID)
                observeButton.text = resources.getString(R.string.follow)
                followers.text = dbHelper.getFollowers(profileID).size.toString()
//                observeButton.setBackgroundColor(ContextCompat.getColor(view.context, R.color.light_green))
            }
        }

        println("PHONE_NUMBER: ${userData.phone_number}")

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
                val mean_rate = round(profileViewViewModel.getMeanRate(reviews) * 10.0 / 10.0)
                dbHelper.updateMeanRate(profileID, mean_rate)
                meanRate.text = mean_rate.toString()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        reportUser = menu.findItem(R.id.menu_reportUser)
        if(profileID == userID)
            reportUser.isVisible = false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                return true
            }
            R.id.menu_reportUser -> {
                Snackbar.make(
                    root.findViewById(R.id.reviewInformation),
                    "Zgłoszono użytkownika",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.menu_likeAd)
//            likeAd = item
//
//        when (item.itemId) {
//            R.id.menu_likeAd -> {
//                addToLiked()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

}