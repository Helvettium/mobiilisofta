package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import java.util.ArrayList

class FavouritesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.favourites_fragment, container, false)
        viewManager = LinearLayoutManager(activity)
        drawStops(view)
        return view
    }

    private fun drawStops(view: View){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        val stopString = preferences.getString("favs_array_stops","[]")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)

            val fetchedData: ArrayList<String> = ArrayList()
            for (i in (0 until stopArray.length())) {

                fetchedData.add(stopArray[i].toString())
            }
            viewAdapter = FavouritesStopsAdapter(fetchedData,activity!!.applicationContext,view)

            recyclerView = view.findViewById<RecyclerView>(R.id.favStopsRecycle).apply {
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
}