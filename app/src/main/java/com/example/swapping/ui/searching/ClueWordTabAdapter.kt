import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.swapping.ui.searching.Ads
import com.example.swapping.ui.searching.Categories
import com.example.swapping.ui.searching.Localizations
import com.example.swapping.ui.searching.Users

@Suppress("DEPRECATION")
internal class ClueWordTabAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Ads()
            }
            1 -> {
                Users()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}