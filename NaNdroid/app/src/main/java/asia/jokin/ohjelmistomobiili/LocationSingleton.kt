package asia.jokin.ohjelmistomobiili

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*


object LocationSingleton {
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var mLastLocation: Location? = null
    lateinit var mLocationRequest: LocationRequest
    lateinit var mLocationCallback: LocationCallback

    private const val INTERVAL: Long = 5000
    private const val FASTEST_INTERVAL: Long = 2000

    //@SuppressLint("MissingPermission")

    fun startUpdates(mContext: Context) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)

        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                mLastLocation = locationResult.lastLocation
            }
        }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    fun getLocation():Location? {
        return mLastLocation
    }

    fun getLat():Double {
        return mLastLocation.latitude
    }

    fun getLng():Double {
        return mLastLocation.longitude
    }
}

/* OLD:
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
*/