package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class RouteResultsActivity : AppCompatActivity() {
    private val stops = arrayOf("Yksi", "kaksi", "Kolme")

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.routes_results_activity)
        viewManager = LinearLayoutManager(this)

        viewAdapter = RoutesResultsCardAdapter(stops)

        FetchDataSingleton.getInstance(this.applicationContext).getRouteResults(object: RoutesDataCallback {
            override fun onSuccess(response: ArrayList<Route>, context: Context) {
                println(response)
            }
        })

        recyclerView = findViewById<RecyclerView>(R.id.routeResultsRecyclerVIew).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }
}
