package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextWatcher
import android.widget.EditText
import android.text.Editable

class RouteLocationPickerActivity : AppCompatActivity() {
    private var stops = arrayListOf<String>("Yksi", "kaksi", "Kolme")

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var searchLocationEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.routes_pick_location_activity)
        viewAdapter = RoutesLocationPickerItemAdapter(stops, { location: String -> locationItemClicked(location) })

        searchLocationEditText = findViewById(R.id.searchLocationEditText)
        searchLocationEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                println(s)
                if(searchLocationEditText.text.length > 2) {
                    // Fire request to get datas
                    val newData = arrayListOf<String>("Yksi", "kaksi", "Kolme")
                    stops.addAll(newData)
                    viewAdapter.notifyDataSetChanged()
                }
            }


            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        viewManager = LinearLayoutManager(this)



        recyclerView = findViewById<RecyclerView>(R.id.locationListRecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(false)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

/*        FetchDataSingleton.getInstance(this.applicationContext).getRouteResults(object: RoutesDataCallback {
            override fun onSuccess(response: List<List<Route>>, context: Context) {
                val fetchedData: List<List<Route>> = response
                println(response)

                viewAdapter = RoutesResultsCardAdapter(fetchedData, context)

                recyclerView = findViewById<RecyclerView>(R.id.locationListRecyclerView).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(false)

                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
            }
        })*/
    }

    private fun locationItemClicked(location : String) {
        println(location)
        val data = Intent()
        data.putExtra("locationName", location)
        setResult(RESULT_OK, data)
        finish()
    }
}
