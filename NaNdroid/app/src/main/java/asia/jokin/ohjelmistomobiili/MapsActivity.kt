package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.json.JSONObject
import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.*
import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

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

    private fun updateStops(mLatLng: LatLng) {
        // Tyhjennetään vanhat merkit
        mMap.clear()

        // Haetaan pysäkit
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(mLatLng, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {

                // Iteroidaan palautettu lista
                for(i in 0 until response.length()) {

                    // Koordinaatit tulee stringinä
                    val coords = response.getJSONObject(i).getString("coords").split(",")
                    val options = MarkerOptions()
                        options.position(LatLng(coords[1].toDouble(), coords[0].toDouble()))
                        options.title(response.getJSONObject(i).getString("name"))

                    val marker = mMap.addMarker(options)
                    marker.tag = response.getJSONObject(i)
                }
            }
        })
    }

    private fun openStop(mCode: String) {
        val popupIntent = Intent(this@MapsActivity, PopupActivity::class.java)
        popupIntent.putExtra("stopid", mCode)
        startActivity(popupIntent)
    }
}