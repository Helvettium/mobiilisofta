package asia.jokin.ohjelmistomobiili

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val COUNT = 4 // fragmenttien/sivujen maara

    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RoutesFragment()
            1 -> fragment = FavouritesFragment()
            2 -> fragment = StopsFragment()
            3 -> fragment = LinesFragment()
        }

        return fragment
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        when (position) {
            0 -> title = "Routes"
            1 -> title = "Favourites"
            2 -> title = "Stops"
            3 -> title = "Lines"
        }
        return title
    }
}