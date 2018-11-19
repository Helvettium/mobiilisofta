package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray

class StopsFragment : Fragment() {

    private val testLocation: LatLng = LatLng(61.4975568, 23.7603378)
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var fetchManager = FetchDataSingleton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.stops_fragment, container, false)

        viewManager = LinearLayoutManager(activity)

        fetchManager.getInstance(activity!!.applicationContext).getFiveClosestStops(testLocation, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                val fetchedData: ArrayList<String> = ArrayList()
                for (i in (0 until response.length())) {

                    fetchedData.add(response[i].toString())
                }
                viewAdapter = StopsAdapter(fetchedData,activity!!.applicationContext)

                recyclerView = view.findViewById<RecyclerView>(R.id.stopsRecycle).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter

                }
            }
        })

        // TODO stops-koodi

        return view
    }
}