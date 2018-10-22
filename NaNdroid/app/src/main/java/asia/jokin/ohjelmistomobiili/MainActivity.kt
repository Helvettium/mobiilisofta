package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.window.decorView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark)) // IDE kaski kayttaa getColor ja sit se on deprecated >:U



        val startupIntent = Intent(this, Favourites::class.java)
        startActivity(startupIntent)
    }
}
