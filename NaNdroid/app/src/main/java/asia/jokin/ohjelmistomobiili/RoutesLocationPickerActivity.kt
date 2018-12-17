package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextWatcher
import android.widget.EditText
import android.text.Editable

class RouteLocationPickerActivity : AppCompatActivity() {
    private lateinit var mData: ArrayList<Point>

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var searchLocationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.routes_pick_location_activity)
        mData = ArrayList()
        mData.add(Point("location", "Current location", "${LocationSingleton.getLng()},${LocationSingleton.getLat()}"))

        searchLocationEditText = findViewById(R.id.searchLocationEditText)
        searchLocationEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println(s)
                if(searchLocationEditText.text.length > 2) {
                    // Fire request to get datas
                    val request = GeocodeDataRequest(searchLocationEditText.text.toString())
                    FetchDataSingleton.getInstance(applicationContext).getLocations(request, object: LocationDataCallback {
                        override fun onSuccess(response: List<Point>, context: Context) {
                            mData.clear()
                            mData.add(Point("location", "Current location", LocationSingleton.getLocation().toString()))
                            mData.addAll(response)
                            viewManager = LinearLayoutManager(applicationContext)
                            viewAdapter = RoutesLocationPickerItemAdapter(mData.toList()) { point: Point -> locationItemClicked(point) }
                            recyclerView = findViewById<RecyclerView>(R.id.locationListRecyclerView).apply {
                                setHasFixedSize(true)
                                layoutManager = viewManager
                                adapter = viewAdapter
                            }
                        }

                        override fun onFailure(error: String, context: Context) {
                            println(error)
                            //val noAlertsSnackbar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Error searching locations: ", Snackbar.LENGTH_SHORT)
                            //noAlertsSnackbar.show()
                        }
                    })
                }
            }


            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        // Set everything so we get non-empty UI until user searches for a location
        viewManager = LinearLayoutManager(applicationContext)
        viewAdapter = RoutesLocationPickerItemAdapter(mData.toList()) { point: Point -> locationItemClicked(point) }
        recyclerView = findViewById<RecyclerView>(R.id.locationListRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun locationItemClicked(point: Point) {
        val data = Intent()
        data.putExtra("locationName", point.name)
        data.putExtra("coords", point.coords)
        setResult(RESULT_OK, data)
        finish()
    }
}
