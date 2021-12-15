import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.swapping.ui.searching.Categories
import com.example.swapping.ui.searching.Localizations

@Suppress("DEPRECATION")
internal class SearchingTabAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Categories()
            }
            1 -> {
                Localizations()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}