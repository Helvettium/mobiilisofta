package asia.jokin.ohjelmistomobiili

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var timerTask: TimerTask
    private var timer = Timer()
    internal val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocationSingleton.startUpdates(this)

        checkPermission()
        setTimerTask()
        timer.schedule(timerTask, 100, 1000)


    }

    private fun checkPermission(){
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)

    }

    private fun getContext(): MainActivity {
        return this
    }

    private fun endTimer(){
        timer.cancel()
        timer.purge()
    }

    private fun setTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    // Every second check permissions
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        checkPermission()
                    }
                    else {
                        val startupIntent = Intent(getContext(), FrontPage::class.java)
                        startActivity(startupIntent)
                        getContext().finish()
                        endTimer()
                    }
                }
            }
        }
    }
}
