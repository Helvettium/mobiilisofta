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