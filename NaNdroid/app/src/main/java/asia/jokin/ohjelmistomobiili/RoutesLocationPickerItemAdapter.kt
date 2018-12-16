package asia.jokin.ohjelmistomobiili

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.routes_pick_location_item.view.*

class RoutesLocationPickerItemAdapter(private val locationList: List<Point>, private val clickListener: (Point) -> Unit) :
        RecyclerView.Adapter<RoutesLocationPickerItemAdapter.LocationResultViewHolder>() {

    class LocationResultViewHolder(private val itemview: ConstraintLayout) : RecyclerView.ViewHolder(itemview) {
        fun bind(point: Point, clickListener: (Point) -> Unit) {
            itemView.routeLocationNameTextView.text = point.name
            val locIcon = if(point.locType == "stop") {
                R.drawable.ic_bus_glyph
            }
            else if(point.locType == "address"){
                R.drawable.ic_street_glyph
            }
            else {
                R.drawable.ic_location_glyph
            }
            itemview.routeLocationTypeImageVIew.setImageResource(locIcon)
            itemView.setOnClickListener { clickListener(point)}
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