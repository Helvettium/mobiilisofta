package asia.jokin.ohjelmistomobiili

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button

import kotlinx.android.synthetic.main.activity_favourites.*

class Favourites : AppCompatActivity() {

    val favButton = findViewById<Button>(R.id.favsButton)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        favButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
