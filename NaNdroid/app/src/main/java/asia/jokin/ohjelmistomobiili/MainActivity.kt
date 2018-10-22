package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         // TODO taustavari latausruuduksi

        // TODO tassa ladataan tiedot APIlta ja asetuksista

        val startupIntent = Intent(this, FrontPage::class.java)
        startActivity(startupIntent)
    }
}
