import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.swapping.ui.searching.Ads
import com.example.swapping.ui.searching.Categories
import com.example.swapping.ui.searching.Localizations
import com.example.swapping.ui.searching.Users

@Suppress("DEPRECATION")
internal class ClueWordTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private final var fragmentList1: ArrayList<Fragment> = ArrayList()
    private final var fragmentTitleList1: ArrayList<String> = ArrayList()
    private var searchText = ""

    // returns which item is selected from arraylist of fragments.
    override fun getItem(position: Int): Fragment {
        return fragmentList1[position]
    }

    // returns which item is selected from arraylist of titles.
    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList1[position]
    }

    // returns the number of items present in arraylist.
    override fun getCount(): Int {
        return fragmentList1.size
    }

    // this function adds the fragment and title in 2 separate  arraylist.
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList1.add(fragment)
        fragmentTitleList1.add(title)
    }

    fun setTextQueryChanged(newText: String) {
        searchText = newText
    }
}