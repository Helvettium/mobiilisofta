package asia.jokin.ohjelmistomobiili

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.math.RoundingMode

class RoutesLegCardAdapter (private val inputData: List<Leg>):
        RecyclerView.Adapter<RoutesLegCardAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoutesLegCardAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_leg_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.cardView.findViewById<TextView>(R.id.legDistanceTextView).text = ""//formatDistance(inputData[position].length)
        holder.cardView.findViewById<TextView>(R.id.depTimeTextView).text = inputData[position].code
        val legIcon = if(inputData[position].type == "walk") {
            R.drawable.ic_walking_glyph
        }
        else {
            R.drawable.ic_bus_glyph
        }
        holder.cardView.findViewById<ImageView>(R.id.legTypeImageView).setImageResource(legIcon)
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