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
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import android.R.attr.password
import android.content.Context
import org.json.JSONArray


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

        
        // Default location values (keskustori)

        // val defLoc: Location = LocationSingleton.mLastLocation
        // val defLat: Double = intent.getDoubleExtra("lat", defLoc.getLatitude())
        // val defLng: Double = intent.getDoubleExtra("lng", defLoc.getLongitude())




        val testLocation = LatLng(61.4980000, 23.7604000)
        val testMarker1 = LatLng(61.4980214, 23.7603118)
        val testMarker2 = LatLng(61.5040000, 23.7593000)
        val testMarker3 = LatLng(61.4960214, 23.7599118)
        val clickableCircle1 = CircleOptions().center(testMarker1).clickable(true).visible(true).radius(20.0)
        val clickableCircle2 = CircleOptions().center(testMarker2).clickable(true).visible(true).radius(20.0)
        val clickableCircle3 = CircleOptions().center(testMarker3).clickable(true).visible(true).radius(20.0)
        mMap.addCircle(clickableCircle1).run{tag = "circle 1"}
        mMap.addCircle(clickableCircle2).run{tag = "circle 2"}
        mMap.addCircle(clickableCircle3).run{tag = "circle 3"}

        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(testLocation, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                // do stuff with response
            }
        })

        /*
        TODO tassa vain esimerkkikoodia, bussit tulevat valmiissa softassa toisaalle
        johonkin funktioon, joka latautuu aina nakyman muuttuessa

        */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 16.0F))

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(defLat, defLng)))

        with(mMap) {
            setOnCircleClickListener {
                val popupIntent = Intent(this@MapsActivity, PopupActivity::class.java)
                popupIntent.putExtra("name", it.tag.toString())
                startActivity(popupIntent)
            }
        }
    }





/*
    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
            Toast.makeText(this, "The user gestured on the map.",
                           Toast.LENGTH_SHORT).show();
        } else if (reason == OnCameraMoveStartedListener
                                .REASON_API_ANIMATION) {
            Toast.makeText(this, "The user tapped something on the map.",
                           Toast.LENGTH_SHORT).show();
        } else if (reason == OnCameraMoveStartedListener
                                .REASON_DEVELOPER_ANIMATION) {
            Toast.makeText(this, "The app moved the camera.",
                           Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraMove() {
        Toast.makeText(this, "The camera is moving.",
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraMoveCanceled() {
        Toast.makeText(this, "Camera movement canceled.",
                       Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {
        Toast.makeText(this, "The camera has stopped moving.",
                       Toast.LENGTH_SHORT).show();
    }
}
*/
}