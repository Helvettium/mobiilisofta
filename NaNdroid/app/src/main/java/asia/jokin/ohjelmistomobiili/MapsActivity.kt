package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import org.json.JSONObject
import android.R.attr.password
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

        // TODO REMOVE v
        val stopid: Int = intent.getIntExtra("stopid", 0)

        if (stopid != 0) {
            val popupIntent = Intent(this, PopupActivity::class.java)
            popupIntent.putExtra("stopid", stopid.toString())
            startActivity(popupIntent)
        }
        // TODO REMOVE ^
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        // Mihin kartan pitäisi osoittaa
        val curLatLng = LatLng(LocationSingleton.getLat(), LocationSingleton.getLng())

        // Kartta itse
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(LocationSingleton.getLat(), LocationSingleton.getLng()), 16.0F))
        mMap.setOnCameraIdleListener(this)
        mMap.setOnMarkerClickListener(this)

        // Päivitetään pysäkit
        updateStops(curLatLng)
        
        
        // Default location values (keskustori)

        // val defLoc: Location = LocationSingleton.mLastLocation
        // val defLat: Double = intent.getDoubleExtra("lat", defLoc.getLatitude())
        // val defLng: Double = intent.getDoubleExtra("lng", defLoc.getLongitude())





/*
        //val testLocation = LatLng(LocationSingleton.getLat(), LocationSingleton.getLng())
        val testMarker1 = LatLng(61.4980214, 23.7603118)
        val testMarker2 = LatLng(61.5040000, 23.7593000)
        val testMarker3 = LatLng(61.4960214, 23.7599118)
        val clickableCircle1 = CircleOptions().center(testMarker1).clickable(true).visible(true).radius(20.0)
        val clickableCircle2 = CircleOptions().center(testMarker2).clickable(true).visible(true).radius(20.0)
        val clickableCircle3 = CircleOptions().center(testMarker3).clickable(true).visible(true).radius(20.0)

        mMap.addCircle(clickableCircle1).run{tag = "0035"}
        mMap.addCircle(clickableCircle2).run{tag = "0035"}
        mMap.addCircle(clickableCircle3).run{tag = "0035"}
*/


        /*
        TODO tassa vain esimerkkikoodia, bussit tulevat valmiissa softassa toisaalle
        johonkin funktioon, joka latautuu aina nakyman muuttuessa
        */
        
        

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(defLat, defLng)))

/*
        with(mMap) {
            setOnCircleClickListener {
                val popupIntent = Intent(this@MapsActivity, PopupActivity::class.java)
                popupIntent.putExtra("stopid", it.tag.toString().toInt()) // TODO mielimmun anna pysäkin code kun sitä käytetään data haussa
                //Toast.makeText(this@MapsActivity, it.tag.toString(),Toast.LENGTH_LONG).show()
                startActivity(popupIntent)
            }
        }
*/
    }


    override fun onCameraIdle() {
        // Minne kartta osoittaa
        val curLatLng = mMap.getCameraPosition().target

        // Tutkintaa onko kartta siirtynyt tarpeeksi?

        // Päivitetään pysäkit
        updateStops(curLatLng)
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


    override fun onMarkerClick(mMarker: Marker?): Boolean {

        //Toast.makeText(context, mMarker.tag.toString(), Toast.LENGTH_LONG).show()
        Log.d("Marker", mMarker?.tag.toString())

        return true
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}