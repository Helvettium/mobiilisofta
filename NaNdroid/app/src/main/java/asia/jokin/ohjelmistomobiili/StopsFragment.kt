package asia.jokin.ohjelmistomobiili

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class StopsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.stops_fragment, container, false)
        //val textView = view.findViewById<TextView>(R.id.txtMain)
        //textView.setText("In stops")

        val exampleDataTable: Array<String> = arrayOf("Stop 1","Stop 2","Stop 3","Stop 4") // TODO remove

        viewManager = LinearLayoutManager(activity)
        viewAdapter = StopsAdapter(exampleDataTable)

        recyclerView = view.findViewById<RecyclerView>(R.id.stopsRecycle).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        // TODO stops-koodi

        return view
    }
}