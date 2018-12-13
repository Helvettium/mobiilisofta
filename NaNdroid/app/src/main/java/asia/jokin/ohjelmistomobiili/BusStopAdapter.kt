package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONObject

class BusStopAdapter (private val inputData: ArrayList<String>, classContext: Context, this_bus: String):
        RecyclerView.Adapter<BusStopAdapter.MyViewHolder>() {
    private val appContext: Context = classContext
    private val thisBus: String = this_bus


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopAdapter.MyViewHolder {
        // create a new view
        val listView = LayoutInflater.from(parent.context)
                .inflate(R.layout.bus_stop_row, parent, false) as ConstraintLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(listView)
    }

    override fun onBindViewHolder(holder: BusStopAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val responseData = JSONObject(inputData[position])
        holder.itemView.findViewById<TextView>(R.id.stopBusDest).text = responseData.getString("name1")
        holder.itemView.findViewById<TextView>(R.id.stopBusNr).text = responseData.getString("code")
        holder.itemView.findViewById<TextView>(R.id.stopBusArrival).text = parseTime(responseData.getString("time"))

        val busStopsContent: ConstraintLayout = holder.itemView.findViewById(R.id.stopLayout)
        busStopsContent.setOnClickListener{
            val clickIntent = Intent(appContext, MapsActivity::class.java)
            clickIntent.putExtra("stopid", thisBus.toInt())
            appContext.startActivity(clickIntent)
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(listView: ConstraintLayout) : RecyclerView.ViewHolder(listView)

    private fun parseTime(timeString: String): String {
        return timeString.substring(0,2)+":"+timeString.substring(2)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}