package asia.jokin.ohjelmistomobiili

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.*

object LocationSingleton {

    //var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private const val INTERVAL: Long = 2000
    private const val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    lateinit var mLocationRequest: LocationRequest


    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        mLastLocation = location
    }

    fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

    }
}
