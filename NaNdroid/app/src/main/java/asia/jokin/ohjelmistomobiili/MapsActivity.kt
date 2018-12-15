package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {
    private var mStops: MutableList<String> = mutableListOf()
    private lateinit var mMap: GoogleMap
    private lateinit var mPopup: PopupFragment
    private lateinit var mLatLng: LatLng

    override fun onCreate(aSavedInstanceState: Bundle?) {
        super.onCreate(aSavedInstanceState)
        setContentView(R.layout.activity_maps)

        // Alustetaan & piilotetaan pysäkki-popup
        mPopup = supportFragmentManager.findFragmentById(R.id.popup) as PopupFragment
        supportFragmentManager.beginTransaction().hide(mPopup).commit()

        // Kartta-fragment, ilmoitus itselle kun se on valmis
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Mihin kartan pitäisi alussa osoittaa
        val lat = intent.getDoubleExtra("lat", LocationSingleton.getLat())
        val lng = intent.getDoubleExtra("lng", LocationSingleton.getLng())
        val mLatLng = LatLng(lat, lng)

        // Kartta itse
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16.0F))
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)

        // Avataan pysäkki jos sellainen on valittu
        val stopCode = intent.getIntExtra("stopid", 0)

        if (stopCode != 0) {
            openStop(stopCode)
        }

        // Haetaan & näytetään lähialueen pysäkit
        updateStops(mLatLng)
    }

    override fun onCameraIdle() {
        // Minne kartta osoittaa kameran pysähtyessä
        val newLatLng = mMap.getCameraPosition().target

        // Tutkintaa onko kartta siirtynyt tarpeeksi
        val distances = FloatArray(2)
        Location.distanceBetween(mLatLng.latitude, mLatLng.longitude, newLatLng.latitude, newLatLng.longitude, distances)

        if (distances[0] > 10.0) {
            mLatLng = newLatLng
            updateStops(newLatLng)
        }
    }

    override fun onMarkerClick(mMarker: Marker): Boolean {
        // Avataan valittu pysäkki, geneerinen tag -> json -> "code"-string -> int
        openStop((mMarker.tag as JSONObject).getString("code").toInt())

        return true
    }

    private fun updateStops(aLatLng: LatLng) {
        // Haetaan pysäkit
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(aLatLng, 1000, object: DataCallback {
            override fun onSuccess(response: JSONArray, context: Context) {

                // Iteroidaan palautettu lista
                for(i in 0 until response.length()) {
                    val stopCode = response.getJSONObject(i).getString("code")

                    // Onko pysäkki jo kartassa?
                    if (!mStops.contains(stopCode)) {

                        // Pysäkki listaan
                        mStops.add(stopCode)

                        // Koordinaatit tulee stringinä
                        val coords = response.getJSONObject(i).getString("coords").split(",")
                        val options = MarkerOptions()
                        options.position(LatLng(coords[1].toDouble(), coords[0].toDouble()))
                        options.title(response.getJSONObject(i).getString("name"))

                        val marker = mMap.addMarker(options)
                        marker.tag = response.getJSONObject(i)
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_stop_marker))
                    }
                }
            }
        })
    }

    private fun openStop(aCode: Int) {
        // Näytetään pysäkin tiedot
        mPopup.showStop(aCode)

        // Jos fragment on vielä piilossa, tuodaan se näkyviin
        if (mPopup.isHidden) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.popup_show, android.R.animator.fade_out)
                    .show(mPopup)
                    .commit()
        }
    }
}