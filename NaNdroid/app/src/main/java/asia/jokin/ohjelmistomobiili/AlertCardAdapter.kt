package asia.jokin.ohjelmistomobiili

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class AlertCardAdapter (private val inputData: ArrayList<Alert>):
        RecyclerView.Adapter<AlertCardAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AlertCardAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.alert_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val timePeriod = "${parseTime(inputData[position].recordedAt)} - ${parseTime(inputData[position].validUntil)}"
        holder.cardView.findViewById<TextView>(R.id.alertTitle).text = timePeriod
        holder.cardView.findViewById<TextView>(R.id.alertText).text = inputData[position].content
    }

    private fun parseTime(time: Long): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("fi"))
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return formatter.format(calendar.time)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}