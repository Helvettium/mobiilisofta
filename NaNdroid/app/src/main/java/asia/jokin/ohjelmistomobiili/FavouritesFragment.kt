package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class FavouritesFragment : Fragment() {
    private lateinit var recyclerView3: RecyclerView
    private lateinit var viewAdapter3: RecyclerView.Adapter<*>
    private lateinit var viewManager3: RecyclerView.LayoutManager
    private lateinit var recyclerView2: RecyclerView
    private lateinit var viewAdapter2: RecyclerView.Adapter<*>
    private lateinit var viewManager2: RecyclerView.LayoutManager
    private lateinit var timerTask: TimerTask
    private var timer = Timer()
    internal val handler = Handler()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.favourites_fragment, container, false)
        viewManager3 = LinearLayoutManager(activity)
        viewManager2 = LinearLayoutManager(activity)
        /*
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)!!
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("favs_array_lines", "[]")
        editor.commit()
        */
        drawStops(view)
        drawLines(view)
        setTimerTask(view)
        timer.schedule(timerTask, 100, 10000)
        return view
    }

    private fun drawStops(view: View){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        viewManager3 = LinearLayoutManager(activity)
        val stopString = preferences.getString("favs_array_stops","[]")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)

            val fetchedData: ArrayList<String> = ArrayList()
            for (i in (0 until stopArray.length())) {

                fetchedData.add(stopArray[i].toString())
            }
            viewAdapter3 = FavouritesStopsAdapter(fetchedData,activity!!.applicationContext,view)

            recyclerView3 = view.findViewById<RecyclerView>(R.id.favStopsRecycle).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager3

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter3

            }
        }
    }
    private fun drawLines(view: View){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        viewManager2 = LinearLayoutManager(activity)
        val stopString = preferences.getString("favs_array_lines","[]")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)

            val fetchedData: ArrayList<String> = ArrayList()
            for (i in (0 until stopArray.length())) {

                fetchedData.add(stopArray[i].toString())
            }
            viewAdapter2 = FavouritesLinesAdapter(fetchedData,activity!!.applicationContext,view)

            recyclerView2 = view.findViewById<RecyclerView>(R.id.favLinesRecycle).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = viewManager2

                // specify an viewAdapter (see also next example)
                adapter = viewAdapter2

            }
        }
    }
    private fun setTimerTask(view: View) {
        timerTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    drawStops(view)
                    drawLines(view)
                }
            }
        }
    }
}