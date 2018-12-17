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
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import java.util.*

class StopsFragment : Fragment() {

    private lateinit var recyclerView7: RecyclerView
    private lateinit var viewAdapter7: RecyclerView.Adapter<*>
    private lateinit var viewManager7: RecyclerView.LayoutManager
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

        viewManager7 = LinearLayoutManager(activity)

        val preference: Int = preferences.getString("nearby","5").toInt()

        fetchManager.getInstance(activity!!.applicationContext).getClosestStops(currentLocation, preference, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                val fetchedData: ArrayList<String> = ArrayList()
                for (i in (0 until response.length())) {

                    fetchedData.add(response[i].toString())
                }
                viewAdapter7 = StopsAdapter(fetchedData,activity!!.applicationContext,view)

                recyclerView7 = view.findViewById<RecyclerView>(R.id.stopsRecycle).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = viewManager7

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter7

                }
            }
        })
    }
    private fun updateStops(view: View){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val currentLocation = LatLng(LocationSingleton.getLat(),LocationSingleton.getLng())

        //viewManager7 = LinearLayoutManager(activity)

        val preference: Int = preferences.getString("nearby","5").toInt()

        fetchManager.getInstance(activity!!.applicationContext).getClosestStops(currentLocation, preference, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                val fetchedData: ArrayList<String> = ArrayList()
                for (i in (0 until response.length())) {

                    fetchedData.add(response[i].toString())
                }

                recyclerView7.swapAdapter(StopsAdapter(fetchedData,activity!!.applicationContext,view),true)

            }
        })
    }

    private fun setTimerTask(view: View) {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    updateStops(view)
                }
            }
        }
    }
}