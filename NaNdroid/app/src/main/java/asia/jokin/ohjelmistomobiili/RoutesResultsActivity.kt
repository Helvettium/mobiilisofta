package asia.jokin.ohjelmistomobiili

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageButton
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap





class RouteResultsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mContext = this
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var mapView: SupportMapFragment
    private lateinit var mMap: GoogleMap

    private lateinit var mData: List<Route>
    private var mapFullScreen: Boolean = false
    private var originalHeight: Int? = 0
    private var originalWidth: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.routes_results_activity)
        viewManager = LinearLayoutManager(this)
        mData = intent.getParcelableArrayListExtra("resultsData")

        viewAdapter = RoutesResultsCardAdapter(mData, this) { route: Route -> locationItemClicked(route) }
        recyclerView = findViewById<RecyclerView>(R.id.routeResultsRecyclerVIew).apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        this.
        mapView = supportFragmentManager.findFragmentById(R.id.routeInfoMap) as SupportMapFragment
        mapView.getMapAsync(mContext)

        val fullScreenMapButton = findViewById<ImageButton>(R.id.fullScreenMapButton)
        fullScreenMapButton.setOnClickListener { v ->
            mapView.view?.requestLayout()
            mapView.view?.layoutParams?.height = this.window.decorView.height
            mapView.view?.layoutParams?.width = this.window.decorView.width
            mapFullScreen = !mapFullScreen
        }
    }

    override fun onBackPressed() {
        if(mapFullScreen) {
            mapView.view?.requestLayout()
            mapView.view?.layoutParams?.height = originalHeight
            mapView.view?.layoutParams?.width = originalWidth
            mapFullScreen = !mapFullScreen
            // setContentView(R.layout.routes_results_activity)
        } else {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val lat = intent.getDoubleExtra("lat", LocationSingleton.getLat())
        val lng = intent.getDoubleExtra("lng", LocationSingleton.getLng())

        val curLatLng = LatLng(lat, lng)

        // Kartta itse
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 16.0F))
        mMap.setOnMapLoadedCallback {
            moveMapToRoute(mData.first())
            originalHeight = mapView.view?.height
            originalWidth = mapView.view?.width
        }
    }

    private fun moveMapToRoute(route: Route) {
        mMap.clear()
        val markers: ArrayList<LatLng> = ArrayList()
        for(leg in route.legs) {
            for(loc in leg.locs) {
                markers.add(LatLng(loc.coord.y, loc.coord.x))
            }
        }
        mMap.addPolyline(PolylineOptions()
                .addAll(markers)
                .width(10f)
                .color(ContextCompat.getColor(applicationContext, R.color.colorPrimary)))
        val builder = LatLngBounds.Builder()
        builder.include(markers.first())
        builder.include(markers[markers.size / 4])
        builder.include(markers[markers.size / 4 * 3])
        builder.include(markers.last())
        val bounds = builder.build()
        val padding = 60
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cu)
    }

    private fun locationItemClicked(route: Route) {
        moveMapToRoute(route)
    }
}
