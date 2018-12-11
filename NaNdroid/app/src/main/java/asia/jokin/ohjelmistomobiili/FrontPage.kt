package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.support.design.widget.Snackbar

class FrontPage : AppCompatActivity() {
    private val fetchManager = FetchDataSingleton
    private var alerts: ArrayList<Alert> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_page)

        val floatingMap = findViewById<FloatingActionButton>(R.id.floatingMapButton)

        floatingMap.setOnClickListener {
            val startupIntent = Intent(this, MapsActivity::class.java)
            startActivity(startupIntent)
        }

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        if (viewPager != null) {
            val adapter = ViewPagerAdapter(supportFragmentManager)
            viewPager.adapter = adapter
        }

        // Get alerts and enable button if there are any
        getAlerts()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chose the "Settings" item, show the app settings UI...
            val startupIntent = Intent(this, SettingsActivity::class.java)
            startActivity(startupIntent)
            true
        }

        R.id.action_alerts -> {
            // User chose the "Alerts" action, mark the current item
            if(!alerts.isEmpty()) {
                val startupIntent = Intent(this, AlertsActivity::class.java)
                startupIntent.putExtra("alertsData", alerts)
                startActivity(startupIntent)
                true
            }
            else {
                val noAlertsSnackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.no_alerts_active, Snackbar.LENGTH_SHORT)
                noAlertsSnackbar.show()
                false
            }
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        this.finishAffinity()
        super.onBackPressed()
    }

    private fun getAlerts() {
        fetchManager.getInstance(this.applicationContext).getGeneralMessages(object: AlertDataCallback {
            override fun onSuccess(response: ArrayList<Alert>, context: Context) {
                alerts = response
            }
        })
    }
}
