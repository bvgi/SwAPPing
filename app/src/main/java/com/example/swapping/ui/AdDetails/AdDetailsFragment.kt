package com.example.swapping.ui.AdDetails

import android.annotation.SuppressLint
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.swapping.R
import com.example.swapping.databinding.FragmentAdDetailsBinding


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

    var userID = 0
    var adID = 0
    var profileID = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        adDetailsViewModel = AdDetailsViewModel()
        userID = args.userID
        adID = args.adID
        profileID = args.profileID

        println("AD FRAGMENT: $userID, $adID, $profileID")

        _binding = FragmentAdDetailsBinding.inflate(inflater, container, false)

        root = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = adDetailsViewModel.getUserInfo(profileID, view.context)
        val ad = adDetailsViewModel.getAd(adID, view.context)
        val photo = adDetailsViewModel.getImage(ad.image)

        title = view.findViewById(R.id.Title)
        title.text = ad.title

        adPhoto = view.findViewById(R.id.AdImage)
        adPhoto.setImageBitmap(photo)

        description = view.findViewById(R.id.adDescription)
        description.text = ad.description

        name = view.findViewById(R.id.adOwnerName)
        name.text = user.name

        username = view.findViewById(R.id.ownerUsername)
        username.text = "@" + user.username

        goToProfile = view.findViewById(R.id.goToUserProfile)
        goToProfile.setOnClickListener {
            val action = AdDetailsFragmentDirections.actionAdDetailsFragmentToProfileViewFragment()
            action.profileID = profileID
            action.userID = userID
            view.findNavController().navigate(action)
        }

        val star1 = view.findViewById<ImageView>(R.id.adStar1)
        val star2 = view.findViewById<ImageView>(R.id.adStar2)
        val star3 = view.findViewById<ImageView>(R.id.adStar3)
        val star4 = view.findViewById<ImageView>(R.id.adStar4)
        val star5 = view.findViewById<ImageView>(R.id.adStar5)
        rateStars = arrayOf(star1, star2, star3, star4, star5)
        adDetailsViewModel.changeStars(rateStars, user.mean_rate)
        println("AD::::${user.ID}, RATE: ${user.mean_rate}")

        location = view.findViewById(R.id.locationName)
        if (ad.city != "-")
            location.text = ad.city + ", " + ad.voivodeship
        else
            location.text = ad.voivodeship

        category = view.findViewById(R.id.adCategory)
        category.text = ad.category

        status = view.findViewById(R.id.adStatus)
        status.text = ad.status
    }
}