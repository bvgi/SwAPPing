package com.example.swapping.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.Models.Review
import com.example.swapping.R
import com.example.swapping.databinding.FragmentProfileViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.math.round

class ProfileViewFragment : Fragment() {

    var userID = 0
    var profileID = 0
    var prev = ""
    var adID = 0

    private lateinit var profileViewViewModel: ProfileViewViewModel
    private var _binding: FragmentProfileViewBinding? = null


    private lateinit var username: TextView
    private lateinit var email: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var observeButton: Button
    private lateinit var addOpinionLayout: LinearLayout
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
    private lateinit var goToUserAds: LinearLayout

    private lateinit var reviews: Array<Review>
    private lateinit var reviewsRecycler: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var reportUser: MenuItem
    private val networkConnection = NetworkConnection()

    var finalrate = 0

    private lateinit var root: View

    val arg: ProfileViewFragmentArgs by navArgs()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arg.let {
            profileID = it.profileID // ID profilu na kt??ry wchodzimy
            userID = it.userID // ID u??ytkownika korzystaj??cego z aplikacji
            prev = it.previous // poprzedni fragment
            adID = it.adID
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
        reviewTitle = root.findViewById(R.id.reviewTitle)

        if(reviews.isEmpty())
            reviewTitle.visibility = View.GONE

        observeButton = root.findViewById(R.id.observeButton)
        addOpinionLayout = root.findViewById(R.id.addOpinionLayout)
        rateDescription = root.findViewById(R.id.reviewContent)
        goToUserAds = root.findViewById(R.id.goToUserAds)


        if (profileID == userID) {
            observeButton.visibility = View.GONE
            addOpinionLayout.visibility = View.GONE
            goToUserAds.visibility = View.GONE
        }

        if(profileViewViewModel.isFollower(userID, profileID, root.context)){
            observeButton.text = getString(R.string.unfollow)
        }
        else{
            observeButton.text = getString(R.string.follow)
        }


        if(profileViewViewModel.isReviewer(userID, profileID, root.context))
            addOpinionLayout.visibility = View.GONE

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reviewsRecycler = view.findViewById(R.id.reviews)
        reviewsRecycler.layoutManager = LinearLayoutManager(view.context)
        reviewsAdapter = ReviewsAdapter(Pair(userID, reviews), view.context)
        reviewsRecycler.isNestedScrollingEnabled = false
        reviewsRecycler.adapter = reviewsAdapter
        reviewsRecycler.addItemDecoration(
            DividerItemDecoration(view.context,
                DividerItemDecoration.VERTICAL)
        )

        reviewsAdapter.setOnClickListener(object : ReviewsAdapter.ReviewsClickListener{
            override fun onClick(pos: Int, aView: View) {
                profileViewViewModel.deleteReview(profileID, userID, view.context)
                Snackbar.make(
                    view.findViewById(R.id.reviewInformation),
                    "Usuni??to ocen??",
                    Snackbar.LENGTH_SHORT
                ).show()
                reviews = profileViewViewModel.getReviews(profileID, root.context)
                profileViewViewModel.updateMeanRate(
                    profileID,
                    round(profileViewViewModel.getMeanRate(reviews) * 10.0 / 10.0),
                    view.context
                )
                meanRate.text = profileViewViewModel.getMeanRate(reviews).toString()
                reviewsAdapter.notifyItemRemoved(pos)
                reviewsAdapter.notifyItemRangeChanged(pos, reviews.size)
                reviewsAdapter.notifyDataSetChanged()
                val action = ProfileViewFragmentDirections.actionNavigationProfileViewSelf()
                action.userID = userID
                action.profileID = profileID
            }
        })

        val userData = profileViewViewModel.getUser(profileID, view.context)

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

        followers.text = profileViewViewModel.getFollowersSize(profileID, view.context).toString()
        followers.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action =
                    ProfileViewFragmentDirections.actionNavigationProfileViewToUsersListFragment()
                action.followersOrFollowing = 0
                action.profileID = profileID
                action.userID = userID
                action.adID = adID
                findNavController().navigate(action)
            }
        }
        following.text = profileViewViewModel.getFollowingSize(profileID, view.context).toString()
        following.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action =
                    ProfileViewFragmentDirections.actionNavigationProfileViewToUsersListFragment()
                action.followersOrFollowing = 1
                action.profileID = profileID
                action.userID = userID
                action.adID = adID
                findNavController().navigate(action)
            }
        }

        observeButton.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (!profileViewViewModel.isFollower(userID, profileID, view.context)) {
                    profileViewViewModel.follow(profileID, userID, view.context)
                    observeButton.text = "Przesta?? obserwowa??"
                    followers.text = profileViewViewModel.getFollowersSize(profileID, view.context).toString()
                } else {
                    profileViewViewModel.unfollow(profileID, userID, view.context)
                    observeButton.text = getString(R.string.follow)
                    followers.text = profileViewViewModel.getFollowersSize(profileID, view.context).toString()
                }
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

        goToUserAds.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val action =
                    ProfileViewFragmentDirections.actionNavigationProfileViewToUserAdsFragment()
                action.userID = userID
                action.previousFragment = "Profile"
                action.profileID = profileID
                action.prevAdID = adID
                findNavController().navigate(action)
            }
        }

        alert = view.findViewById(R.id.rateAlert)

        addReviewButton = view.findViewById(R.id.saveReviewButton)
        addReviewButton.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dost??pu do Internetu",
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
                    profileViewViewModel.addReview(review, view.context)
                    reviews = mutableListOf(review).toTypedArray() + reviews
                    reviewsAdapter.notifyItemInserted(0)
                    reviewsAdapter.notifyItemRangeChanged(0, reviews.size)
                    reviewsRecycler.adapter = reviewsAdapter
                    val mean_rate = profileViewViewModel.getMeanRate(reviews)
                    profileViewViewModel.updateMeanRate(profileID, mean_rate, view.context)
                    meanRate.text = mean_rate.toString()
                    Snackbar.make(
                        view.findViewById(R.id.reviewInformation),
                        "Ocena zosta??a dodana",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    val action = ProfileViewFragmentDirections.actionNavigationProfileViewSelf()
                    action.userID = userID
                    action.profileID = profileID
                    view.findNavController().navigate(action)
                } else {
                    alert.visibility = View.VISIBLE
                }
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
        if(item.itemId == R.id.menu_reportUser){
            reportUser = item
        }
        when (item.itemId){
            R.id.menu_reportUser -> {
                if (!networkConnection.isNetworkAvailable(requireView().context)) {
                    Snackbar.make(
                        requireView().findViewById(R.id.noInternet),
                        "Brak dost??pu do Internetu",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        root.findViewById(R.id.reviewInformation),
                        "Zg??oszono u??ytkownika",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                return true
            }
            android.R.id.home -> {
                if(userID == profileID) {
                    val action =
                        ProfileViewFragmentDirections.actionNavigationProfileViewToNavigationProfile()
                    action.userID = userID
                    findNavController().navigate(action)
                } else {
                    when(prev) {
                        "Liked" -> {
                            val action =
                                ProfileViewFragmentDirections.actionNavigationProfileViewToAdDetailsFragment()
                            action.userID = userID
                            action.adID = adID
                            action.profileID = profileID
                            action.previousFragment = "Liked"
                            findNavController().navigate(action)
                        }
                        "Followers" -> {
                            val action = ProfileViewFragmentDirections.actionNavigationProfileViewToUsersListFragment()
                            action.followersOrFollowing = 0
                            action.profileID = profileID
                            action.userID = userID
                            findNavController().navigate(action)
                        }
                        "Following" -> {
                            val action =
                                ProfileViewFragmentDirections.actionNavigationProfileViewToUsersListFragment()
                            action.followersOrFollowing = 1
                            action.profileID = profileID
                            action.userID = userID
                            findNavController().navigate(action)
                        }
                        else -> {
                        val action =
                            ProfileViewFragmentDirections.actionNavigationProfileViewToAdDetailsFragment()
                        action.userID = userID
                        action.adID = adID
                        action.profileID = profileID
                        action.previousFragment = "Profile"
                        findNavController().navigate(action)
                    }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}