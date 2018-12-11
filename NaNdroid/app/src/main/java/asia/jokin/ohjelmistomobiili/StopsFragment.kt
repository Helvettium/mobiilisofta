package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import java.util.*

class StopsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var fetchManager = FetchDataSingleton
    private lateinit var timerTask: TimerTask
    private var timer = Timer()
    internal val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.stops_fragment, container, false)

        drawStops(view)

        setTimerTask(view)
        timer.schedule(timerTask, 100, 10000)
        return view
    }

    private fun drawStops(view: View){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        val currentLocation = LatLng(LocationSingleton.getLat(),LocationSingleton.getLng())
        //Toast.makeText(this@StopsFragment.activity,currentLocation.latitude.toString()+", "+currentLocation.longitude.toString(),Toast.LENGTH_LONG).show()

        viewManager = LinearLayoutManager(activity)

        val preference: Int = preferences.getString("nearby","5").toInt()

        fetchManager.getInstance(activity!!.applicationContext).getClosestStops(currentLocation, preference, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                val fetchedData: ArrayList<String> = ArrayList()
                for (i in (0 until response.length())) {

                    fetchedData.add(response[i].toString())
                }
                viewAdapter = StopsAdapter(fetchedData,activity!!.applicationContext,view)

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
    }

    private fun setTimerTask(view: View) {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    drawStops(view)
                }
            }
        }
    }
}