package asia.jokin.ohjelmistomobiili

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import asia.jokin.ohjelmistomobiili.ui.viewpager.ViewPagerFragment

class ViewPager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_pager_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ViewPagerFragment.newInstance())
                    .commitNow()
        }
    }

}
