package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class StopsAdapter (private val inputData: ArrayList<String>, classContext: Context):
        RecyclerView.Adapter<StopsAdapter.MyViewHolder>() {
    private val appContext: Context = classContext

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
        val responseData = JSONObject(inputData[position])
        val departureDataString: String = responseData.getString("departures")

        if (departureDataString!=""&&inputData.size == 5) {
            val departureData: JSONArray = responseData.getJSONArray("departures")
            val dataBus1: JSONObject = departureData.getJSONObject(0)
            val dataBus2: JSONObject = departureData.getJSONObject(1)
            val dataBus3: JSONObject = departureData.getJSONObject(2)
            val dataBus4: JSONObject = departureData.getJSONObject(3)
            val dataBus5: JSONObject = departureData.getJSONObject(4)
            holder.cardView.findViewById<TextView>(R.id.stopBusDest1).text = dataBus1.getString("name1")
            holder.cardView.findViewById<TextView>(R.id.stopBusDest2).text = dataBus2.getString("name1")
            holder.cardView.findViewById<TextView>(R.id.stopBusDest3).text = dataBus3.getString("name1")
            holder.cardView.findViewById<TextView>(R.id.stopBusDest4).text = dataBus4.getString("name1")
            holder.cardView.findViewById<TextView>(R.id.stopBusDest5).text = dataBus5.getString("name1")
            holder.cardView.findViewById<TextView>(R.id.stopBusNr1).text = dataBus1.getString("code")
            holder.cardView.findViewById<TextView>(R.id.stopBusNr2).text = dataBus2.getString("code")
            holder.cardView.findViewById<TextView>(R.id.stopBusNr3).text = dataBus3.getString("code")
            holder.cardView.findViewById<TextView>(R.id.stopBusNr4).text = dataBus4.getString("code")
            holder.cardView.findViewById<TextView>(R.id.stopBusNr5).text = dataBus5.getString("code")
            holder.cardView.findViewById<TextView>(R.id.stopBusArrival1).text = parseTime(dataBus1.getString("time"))
            holder.cardView.findViewById<TextView>(R.id.stopBusArrival2).text = parseTime(dataBus2.getString("time"))
            holder.cardView.findViewById<TextView>(R.id.stopBusArrival3).text = parseTime(dataBus3.getString("time"))
            holder.cardView.findViewById<TextView>(R.id.stopBusArrival4).text = parseTime(dataBus4.getString("time"))
            holder.cardView.findViewById<TextView>(R.id.stopBusArrival5).text = parseTime(dataBus5.getString("time"))
        }
        holder.cardView.findViewById<TextView>(R.id.stopName).text = responseData.getString("name_fi")

        val cardContent:ConstraintLayout = holder.cardView.findViewById(R.id.stopContent)
        cardContent.setOnClickListener {
            val clickIntent = Intent(appContext, MapsActivity::class.java)
            clickIntent.putExtra("stopid", responseData.getString("code").toInt())
            appContext.startActivity(clickIntent)
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    private fun parseTime(timeString: String): String {
        return timeString.substring(0,2)+":"+timeString.substring(2)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}