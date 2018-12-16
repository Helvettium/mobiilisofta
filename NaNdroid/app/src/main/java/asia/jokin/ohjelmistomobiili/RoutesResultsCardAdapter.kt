package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.opengl.Visibility
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.route_card.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RoutesResultsCardAdapter (private val inputData: List<Route>, context: Context):
        RecyclerView.Adapter<RoutesResultsCardAdapter.MyViewHolder>() {
    private val mContext = context
    private var mInfoVisible = false
    private var mResultSelected = false

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoutesResultsCardAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    inner class CustomGridLayoutManager(context: Context, orientation: Int) : LinearLayoutManager(context, orientation, false) {
        private var isScrollEnabled = true

        fun setScrollEnabled(flag: Boolean) {
            this.isScrollEnabled = flag
        }

        override fun canScrollVertically(): Boolean {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically()
        }

        override fun canScrollHorizontally(): Boolean {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollHorizontally()
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(position.equals(0)) {
            mResultSelected = true
        }
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardView.findViewById<TextView>(R.id.routeDepartsTextView).text = parseTime(inputData[position].legs.first().locs.first().depTime)
        holder.cardView.findViewById<TextView>(R.id.routeArrivesTextView).text = parseTime(inputData[position].legs.last().locs.last().arrTime)

        val legsViewManager = CustomGridLayoutManager(this.mContext, LinearLayoutManager.HORIZONTAL)
        legsViewManager.setScrollEnabled(false)
        val legsViewAdapter = RoutesLegCardAdapter(inputData[position].legs)
        val legsRecyclerView = holder.cardView.findViewById<RecyclerView>(R.id.routeLegsRecyclerView)
        legsRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = legsViewManager
            adapter = legsViewAdapter
        }
        val infoViewManager = LinearLayoutManager(this.mContext)

        val selectedIndicator = holder.cardView.findViewById<ImageView>(R.id.resultSelectedmageView)
        if(mResultSelected) {
            selectedIndicator.setBackgroundColor(mContext.getColor(R.color.colorPrimary))
        }
        else {
            selectedIndicator.visibility = View.GONE
        }

        val infoViewAdapter = RoutesInfoCardAdapter(combineLegs(inputData[position].legs))
        val infoRecyclerView = holder.cardView.findViewById<RecyclerView>(R.id.routeInfoRecyclerView)
        infoRecyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            layoutManager = infoViewManager
            adapter = infoViewAdapter
        }
        infoRecyclerView.visibility = View.GONE
        // TODO Change arrow when view is open
        holder.cardView.findViewById<ImageButton>(R.id.showRouteButton).setOnClickListener {
            if(!mInfoVisible) {
                infoRecyclerView.visibility = View.VISIBLE
                mInfoVisible = !mInfoVisible
            }
            else {
                infoRecyclerView.visibility = View.GONE
                mInfoVisible = !mInfoVisible
            }
        }
    }

    private fun combineLegs(legs: List<Leg>): ArrayList<Pair<String?, Location>> {
        var iterator = 0
        val locs = ArrayList<Pair<String?, Location>>()
        while(iterator < legs.lastIndex + 1) {
            val type = legs[iterator].code
            var jiterator = 0
            while(jiterator < legs[iterator].locs.lastIndex + 1) {
                locs.add(Pair(type, legs[iterator].locs[jiterator]))
                jiterator++
            }
            iterator++
        }
        return locs
    }

    private fun parseTime(time: String): String {
        val parser = SimpleDateFormat("yyyyMMddHHmm", Locale("fi"))
        val formatter = SimpleDateFormat("HH:mm", Locale("fi"))
        val calendar = Calendar.getInstance()
        calendar.time = parser.parse(time)
        return formatter.format(calendar.time)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}