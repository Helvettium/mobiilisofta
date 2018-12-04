package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class StopsAdapter (private val inputData: ArrayList<String>, classContext: Context, view: View):
        RecyclerView.Adapter<StopsAdapter.MyViewHolder>() {
    private val appContext: Context = classContext

    private val preferences = PreferenceManager.getDefaultSharedPreferences(classContext)!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopsAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.stop_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: StopsAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewManager = LinearLayoutManager(appContext)

        val responseData = JSONObject(inputData[position])
        val departureDataString: String = responseData.getString("departures")
        if (departureDataString!="") {

            val departureData: JSONArray = responseData.getJSONArray("departures")

            val preference: Int = preferences.getString("times_shown","5").toInt()


            val data: ArrayList<String> = ArrayList()
            var maxStops = departureData.length()
            if (maxStops>preference) maxStops = preference

            for (i in (0 until maxStops)) {
                data.add(departureData[i].toString())
            }

            viewAdapter = BusStopAdapter(data,appContext)
            recyclerView = holder.cardView.findViewById<RecyclerView>(R.id.stopRecycle).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
        holder.cardView.findViewById<TextView>(R.id.stopName).text = responseData.getString("name_fi")

        val cardContent:ConstraintLayout = holder.cardView.findViewById(R.id.stopContent)
        cardContent.setOnClickListener {
            val clickIntent = Intent(appContext, MapsActivity::class.java)
            clickIntent.putExtra("stopid", responseData.getString("code").toInt())
            appContext.startActivity(clickIntent)
        } // TODO sisainen adapteri ei sisally, eli klikkaus ei onnistu kokonaan
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}