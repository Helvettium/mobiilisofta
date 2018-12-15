package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject

class FavouritesStopsAdapter (private val inputData: ArrayList<String>, classContext: Context, view: View):
        RecyclerView.Adapter<FavouritesStopsAdapter.MyViewHolder>() {
    private val appContext: Context = classContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesStopsAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.favs_stop_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: FavouritesStopsAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val responseData = JSONObject(inputData[position])

        holder.cardView.findViewById<TextView>(R.id.lineName).text = responseData.getString("name")
        //holder.cardView.findViewById<TextView>(R.id.linesList).text = responseData.getString("lines")
        holder.cardView.findViewById<ImageView>(R.id.favStar).setImageResource(R.drawable.ic_star_select)

        val cardContent: ConstraintLayout = holder.cardView.findViewById(R.id.stopContent)
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


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}