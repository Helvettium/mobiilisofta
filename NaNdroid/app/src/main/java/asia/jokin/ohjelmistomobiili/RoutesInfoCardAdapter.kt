package asia.jokin.ohjelmistomobiili

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class RoutesInfoCardAdapter (private val inputData: ArrayList<Pair<String?, Location>>):
        RecyclerView.Adapter<RoutesInfoCardAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoutesInfoCardAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_info_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.findViewById<TextView>(R.id.lineCodeTextView).text = inputData[position].first
        holder.cardView.findViewById<TextView>(R.id.depTimeTextView).text = parseTime(inputData[position].second.depTime)
        holder.cardView.findViewById<TextView>(R.id.locNameTextView).text = inputData[position].second.name
        val legIcon = if(inputData[position].first == null) {
            R.drawable.ic_walking_glyph
        }
        else {
            R.drawable.ic_bus_glyph
        }
        holder.cardView.findViewById<ImageView>(R.id.legTypeImageView).setImageResource(legIcon)
    }

    private fun parseTime(time: String): String {
        val parser = SimpleDateFormat("yyyyMMddHHmm", Locale("fi"))
        val formatter = SimpleDateFormat("HH:mm", Locale("fi"))
        val calendar = Calendar.getInstance()
        calendar.time = parser.parse(time)
        return formatter.format(calendar.time)
    }

    private fun formatDistance(distance: Double): String {
        return if(distance < 1000) {
            "$distance m"
        } else {
            "${(distance / 1000).toBigDecimal().setScale(1, RoundingMode.UP).toDouble() } km"
        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}