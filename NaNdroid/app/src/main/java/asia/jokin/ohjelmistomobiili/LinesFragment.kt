package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import android.view.KeyEvent.KEYCODE_ENTER
import org.json.JSONArray
import java.util.ArrayList


class LinesFragment : Fragment() {
    var fetchManager = FetchDataSingleton
    private lateinit var searchLine: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.lines_fragment, container, false)
        var stopID: String

        viewManager = LinearLayoutManager(activity)

        searchLine = view.findViewById(R.id.lineSearch)
        searchLine.setOnKeyListener (View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                //Log.e("useful","done with "+searchLine.text.toString())
                stopID = searchLine.text.toString()
                fetchManager.getInstance(activity!!.applicationContext).getLineData(stopID, object: DataCallback{
                    override fun onSuccess(response: JSONArray, context: Context) {
                        val fetchedData: ArrayList<String> = ArrayList()
                        for (i in (0 until response.length())) {

                            fetchedData.add(response[i].toString())
                        }
                        viewAdapter = LineSingleAdapter(fetchedData,activity!!.applicationContext)
                        recyclerView = view.findViewById<RecyclerView>(R.id.lineList).apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                    }
                })
                return@OnKeyListener true
            }
            false
        })

        /*

        */

        return view
    }


}