package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.popup_card.view.*

class PopupAdapter (val lines : ArrayList<String>, val arrivals : ArrayList<String>, val context: Context): RecyclerView.Adapter<PopupAdapter.ViewHolder>() {
    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return lines.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.popup_card, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.dest?.text = lines.get(position)
        holder?.arrival?.text = arrivals.get(position)
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val dest = view.stopBusDest
        val arrival = view.stopBusArrival
    }
}

