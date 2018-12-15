package asia.jokin.ohjelmistomobiili

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.json.JSONObject
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.*
import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {
    private var mStops: MutableList<String> = mutableListOf()
    private lateinit var mMap: GoogleMap
    private lateinit var mPopup: PopupFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Alustetaan muuttujat, piilotetaan pysäkki-popup
        mPopup = supportFragmentManager.findFragmentById(R.id.popup) as PopupFragment
        supportFragmentManager.beginTransaction().hide(mPopup).commit()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Mihin kartan pitäisi alussa osoittaa
        val lat = intent.getDoubleExtra("lat", LocationSingleton.getLat())
        val lng = intent.getDoubleExtra("lng", LocationSingleton.getLng())

        val curLatLng = LatLng(lat, lng)

        // Kartta itse
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LocationSingleton.getLat(), LocationSingleton.getLng()), 16.0F))
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)

        // Avataan mahdollinen valittu pysäkki
        val stopCode = intent.getIntExtra("stopid", 0)

        if (stopCode != 0) {
            openStop(stopCode.toString())
        }

        // Päivitetään pysäkit
        updateStops(curLatLng)

        // Toast.makeText(context, mMarker.tag.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCameraIdle() {
        // Minne kartta osoittaa
        val curLatLng = mMap.getCameraPosition().target

        // Tutkintaa onko kartta siirtynyt tarpeeksi?

        // Päivitetään pysäkit
        updateStops(curLatLng)
    }

    override fun onMarkerClick(mMarker: Marker): Boolean {
        // Debug
        Log.d("Marker", mMarker.tag.toString())

        openStop((mMarker.tag as JSONObject).getString("code"))

        return true
    }

    private fun updateStops(aLatLng: LatLng) {
        // Haetaan pysäkit
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(aLatLng, 1000, object: DataCallback{
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

    private fun openStop(aCode: String) {

        //Toast.makeText(this, "click", Toast.LENGTH_LONG).show()

        // Vitun raivostuttava tapa viitata ID
        // popupTitle.text = aCode

        //val popupFragment = supportFragmentManager.findFragmentById(R.id.popup) as PopupFragment
        //supportFragmentManager.beginTransaction().hide(popupFragment).commit()

        mPopup.showStop(aCode)

        if (mPopup.isHidden) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.animator.popup_show, android.R.animator.fade_out)
                    .show(mPopup)
                    .commit()
        }
    }
}