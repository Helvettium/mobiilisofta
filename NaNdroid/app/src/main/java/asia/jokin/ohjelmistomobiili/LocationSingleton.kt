package asia.jokin.ohjelmistomobiili

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*

object LocationSingleton {
    // Virhe, mutta context tulee olemaan applicationContext joten sen tallentaminen pitäisi olla ok
    @SuppressLint("StaticFieldLeak")
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val mLocationRequest: LocationRequest = LocationRequest()
    private val mLocationCallback: LocationCallback

    private var mLastLocation: Location? = null
    private const val INTERVAL: Long = 5000
    private const val FASTEST_INTERVAL: Long = 2000

    init {
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL

        mLocationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                mLastLocation = locationResult.lastLocation
            }
        }
    }

    fun startUpdates(aContext: Context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(aContext)

        // Varmistetaan että on oikeudet, aloitetaan paikkatietojen vastaanottaminen
        if (ContextCompat.checkSelfPermission(aContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    fun stopUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    fun getLocation(): Location? {
        return mLastLocation
    }

    fun getLat(): Double {
        return if (mLastLocation == null) 61.4980000 else mLastLocation!!.latitude
    }

    fun getLng(): Double {
        return if (mLastLocation == null) 23.7604000 else mLastLocation!!.longitude
    }
}

/* OLD BACKUP:
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