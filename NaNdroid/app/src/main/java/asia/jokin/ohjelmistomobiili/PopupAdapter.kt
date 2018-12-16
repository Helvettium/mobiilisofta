package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.popup_card.view.*
import org.json.JSONArray

//class PopupAdapter (private val lines : ArrayList<String>, private val arrivals : ArrayList<String>, val context: Context): RecyclerView.Adapter<PopupAdapter.ViewHolder>() {

class PopupAdapter(private var aJSONData: JSONArray): RecyclerView.Adapter<PopupAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return aJSONData.length()
    }

    // Inflates the item views
    override fun onCreateViewHolder(aParent: ViewGroup, aViewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(aParent.context).inflate(R.layout.popup_row, aParent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dest?.text = lines[position]
        holder.arrival?.text = arrivals[position]
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val dest = view.stopBusDest!!
        val arrival = view.stopBusArrival!!
    }
}