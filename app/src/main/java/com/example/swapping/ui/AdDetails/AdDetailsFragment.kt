package com.example.swapping.ui.AdDetails


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.databinding.FragmentAdDetailsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class AdDetailsFragment : Fragment() {
    val args: AdDetailsFragmentArgs by navArgs()

    private lateinit var root: View
    private var _binding: FragmentAdDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adDetailsViewModel: AdDetailsViewModel

    private lateinit var title: TextView
    private lateinit var adPhoto: ImageView
    private lateinit var name: TextView
    private lateinit var username: TextView
    private lateinit var description: TextView
    private lateinit var location: TextView
    private lateinit var category: TextView
    private lateinit var status: TextView
    private lateinit var rateStars: Array<ImageView>
    private lateinit var goToProfile: ImageView
    private lateinit var startNegotiation: FloatingActionButton
    private lateinit var publishedDate: TextView

    private lateinit var likeAd: MenuItem
    private lateinit var dbHelper : DataBaseHelper
    private val networkConnection = NetworkConnection()

    var userID = 0
    var adID = 0
    var profileID = 0
    var prev = ""
    var archived = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        args.let {
            profileID = it.profileID // ID profilu na który wchodzimy
            userID = it.userID // ID użytkownika korzystającego z aplikacji
            adID = it.adID // ID ogłoszenia
            prev = it.previousFragment // poprzedni fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        adDetailsViewModel = AdDetailsViewModel()

        _binding = FragmentAdDetailsBinding.inflate(inflater, container, false)

        root = binding.root

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = adDetailsViewModel.getUserInfo(profileID, view.context)
        val ad = adDetailsViewModel.getAd(adID, view.context)
        val photo = adDetailsViewModel.getImage(ad.image)

        dbHelper = DataBaseHelper(view.context)

        val userNegotiations = dbHelper.getUserNegotiations(userID)

        title = view.findViewById(R.id.Title)
        title.text = ad.title

        adPhoto = view.findViewById(R.id.AdImage)
        adPhoto.setImageBitmap(photo)

        publishedDate = view.findViewById(R.id.publishedDate)
        publishedDate.text = "${ad.published_date.toString().substring(6,8)}.${ad.published_date.toString().substring(4,6)}.${ad.published_date.toString().substring(0,4)}"


        description = view.findViewById(R.id.adDescription)
        description.text = ad.description

        name = view.findViewById(R.id.adOwnerName)
        name.text = user.name

        username = view.findViewById(R.id.ownerUsername)
        username.text = "@" + user.username

        goToProfile = view.findViewById(R.id.goToUserProfile)
        goToProfile.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (prev == "Liked") {
                    val action =
                        AdDetailsFragmentDirections.actionAdDetailsFragmentToNavigationProfileView()
                    action.userID = userID
                    action.profileID = profileID
                    action.previous = "Liked"
                    action.adID = adID
                    findNavController().navigate(action)
                } else {
                    val action =
                        AdDetailsFragmentDirections.actionAdDetailsFragmentToNavigationProfileView()
                    action.profileID = profileID
                    action.userID = userID
                    action.adID = adID
                    view.findNavController().navigate(action)
                }
            }

        }

        val star1 = view.findViewById<ImageView>(R.id.adStar1)
        val star2 = view.findViewById<ImageView>(R.id.adStar2)
        val star3 = view.findViewById<ImageView>(R.id.adStar3)
        val star4 = view.findViewById<ImageView>(R.id.adStar4)
        val star5 = view.findViewById<ImageView>(R.id.adStar5)
        rateStars = arrayOf(star1, star2, star3, star4, star5)
        adDetailsViewModel.changeStars(rateStars, user.mean_rate)

        location = view.findViewById(R.id.locationName)
        if (ad.city != "-")
            location.text = ad.city + ", " + ad.voivodeship
        else
            location.text = ad.voivodeship

        category = view.findViewById(R.id.adCategory)
        category.text = ad.category

        status = view.findViewById(R.id.adStatus)
        status.text = ad.status

        startNegotiation = view.findViewById(R.id.startNegotiationButton)
        var isInNegotiation = false
        for(pair in userNegotiations){
            if(pair.second.adID == adID && pair.second.purchaserID == userID)
                isInNegotiation = true
        }
        if(dbHelper.getUserAnnouncements(userID).isEmpty() || isInNegotiation)
            startNegotiation.isEnabled = false
        startNegotiation.setOnClickListener {
            if (!networkConnection.isNetworkAvailable(view.context)) {
                Snackbar.make(
                    view.findViewById(R.id.noInternet),
                    "Brak dostępu do Internetu",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (ad.purchaser_id != userID) {
                    val action =
                        AdDetailsFragmentDirections.actionAdDetailsFragmentToUserAdsActivity()
                    action.profileID = profileID
                    action.userID = userID
                    action.adID = adID
                    view.findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_ad_details, menu)
        likeAd = menu.findItem(R.id.menu_likeAd)
        if (adDetailsViewModel.isLiked(userID, adID, requireContext()))
            likeAd.setIcon(R.drawable.ic_baseline_favorite_24)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_likeAd)
            likeAd = item

        when (item.itemId) {
            R.id.menu_likeAd -> {
                adDetailsViewModel.addToLiked(userID, adID, likeAd, requireContext())
                return true
            }
            android.R.id.home -> {
                if(prev == "Liked"){
                    val action =
                        AdDetailsFragmentDirections.actionAdDetailsFragmentToUserAdsFragment()
                    action.userID = userID
                    action.profileID = userID
                    action.previousFragment = "Liked"
                    findNavController().navigate(action)
                } else {
                    val action =
                        AdDetailsFragmentDirections.actionAdDetailsFragmentToNavigationHome()
                    action.userID = userID
                    findNavController().navigate(action)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}