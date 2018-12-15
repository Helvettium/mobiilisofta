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
import android.widget.Toast
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.popup_fragment.*
import org.json.JSONArray

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {
    private var mStops: MutableList<String> = mutableListOf()
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Piilotetaan pysäkki-popup
        val manager = fragmentManager
        val popup = manager.findFragmentById(R.id.popup)
        manager.beginTransaction().hide(popup).commit()

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
        // Tyhjennetään vanhat merkit  -  Tutkitaan onko parempi jättää lojumaan?
        // mMap.clear()

        // Haetaan pysäkit
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(mLatLng, 1000, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {

                // Iteroidaan palautettu lista
                for(i in 0 until response.length()) {
                    val mCode = response.getJSONObject(i).getString("code")

                    // Onko pysäkki jo kartassa?
                    if (!mStops.contains(mCode)) {

                        // Pysäkki listaan
                        mStops.add(mCode)

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

        Toast.makeText(this, "click", Toast.LENGTH_LONG).show()

        // Vitun raivostuttava tapa viitata ID
        popupTitle.text = aCode

        val manager = fragmentManager
        val fragment = manager.findFragmentById(R.id.popup)

       // val ft = supportFragmentManager.beginTransaction()
        //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

        if (fragment.isHidden) {
            manager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(fragment)
                    .commit()
        }
        else {
            manager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .hide(fragment)
                    .commit()
        }



        /*
        val firstFragment = PopupFragment()
        firstFragment.arguments = intent.extras
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.layout.activity_maps, firstFragment)
        transaction.commit()

        val popupIntent = Intent(this@MapsActivity, PopupActivity::class.java)
        popupIntent.putExtra("stopid", mCode.toInt())
        startActivity(popupIntent)
        */
    }
}