package asia.jokin.ohjelmistomobiili

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.routes_pick_location_item.view.*

class RoutesLocationPickerItemAdapter(private val locationList: ArrayList<String>, private val clickListener: (String) -> Unit) :
        RecyclerView.Adapter<RoutesLocationPickerItemAdapter.LocationResultViewHolder>() {

    class LocationResultViewHolder(val itemview: ConstraintLayout) : RecyclerView.ViewHolder(itemview) {
        fun bind(name: String, clickListener: (String) -> Unit) {
            itemView.routeLocationNameTextView.text = name
            itemView.setOnClickListener { clickListener(name)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoutesLocationPickerItemAdapter.LocationResultViewHolder {
        // create a new view
        val itemview = LayoutInflater.from(parent.context)
                .inflate(R.layout.routes_pick_location_item, parent, false) as ConstraintLayout
        return LocationResultViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: LocationResultViewHolder, position: Int) {
        holder.bind(locationList[position], clickListener)
    }

    override fun getItemCount() = locationList.size
}