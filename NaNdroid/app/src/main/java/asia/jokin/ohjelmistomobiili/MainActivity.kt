package asia.jokin.ohjelmistomobiili

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions,0)

        if (!LocationSingleton.checkPermissionForLocation(this)){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),10)
        } // TODO aktiviteetti ei jää odottamaan vastausta


        // TODO taustavari latausruutuun

        // TODO tassa ladataan tiedot APIlta ja asetuksista

        // TODO tuhoa tama aktiviteetti kun sirrytaan toiseen intentiin, ettei tahan voi palata
        val startupIntent = Intent(this, FrontPage::class.java)
        startActivity(startupIntent)
    }



}
