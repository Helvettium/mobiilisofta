package asia.jokin.ohjelmistomobiili

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class PopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        val textViewName: TextView = findViewById(R.id.textViewName)

        val activityName = intent.extras.getString("name","not_found")

        textViewName.text = activityName
    }
}
