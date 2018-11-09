package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap = googleMap
        val tampere = LatLng(61.4980214, 23.7603118)
        mMap.mapType = MAP_TYPE_NORMAL
        val clickableCircle = CircleOptions().center(tampere).clickable(true).visible(true).strokeColor(R.color.colorPrimary).radius(20.0)
        mMap.addCircle(clickableCircle).run{tag = "markertag"}

        /*
        TODO tassa vain esimerkkikoodia, bussit tulevat valmiissa softassa toisaalle
        johonkin funktioon, joka latautuu aina nakyman muuttuessa

        */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tampere, 16.0F))

        with(mMap){
            setOnCircleClickListener{
                val startupIntent = Intent(this@MapsActivity, TempPopupActivity::class.java)
                startActivity(startupIntent)
            }
        }
    }

}

