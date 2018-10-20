package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().decorView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark)) // IDE kaski kayttaa getColor ja sit se on deprecated >:U

        //Tassa valissa haetaan tiedot ja odotellaan kaynnistymista

        Thread.sleep(2000) // TODO poista kun lisataan jotakin tekemista talle aktiviteetille

        val startupIntent = Intent(this, Favourites::class.java)
        startActivity(startupIntent)
    }
}
