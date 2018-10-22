package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import asia.jokin.ohjelmistomobiili.ui.viewpager.ViewPagerFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         // IDE kaski kayttaa getColor ja sit se on deprecated >:U



        val startupIntent = Intent(this, ViewPager::class.java)
        startActivity(startupIntent)
    }
}
