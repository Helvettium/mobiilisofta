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

class RoutesResultsCardAdapter (private val inputData: List<Route>, context: Context, private val clickListener: (Route) -> Unit):
        RecyclerView.Adapter<RoutesResultsCardAdapter.RouteResultViewHolder>() {
    private val mContext = context

    class RouteResultViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
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

        inner class CustomGridLayoutManager(context: Context, orientation: Int) : LinearLayoutManager(context, orientation, false) {
            private var isScrollEnabled = true

            fun setScrollEnabled(flag: Boolean) {
                this.isScrollEnabled = flag
            }

            override fun canScrollVertically(): Boolean {
                return isScrollEnabled && super.canScrollVertically()
            }

            override fun canScrollHorizontally(): Boolean {
                return isScrollEnabled && super.canScrollHorizontally()
            }
        }

        fun bind(route: Route, context: Context, clickListener: (Route) -> Unit) {
            cardView.findViewById<TextView>(R.id.routeDepartsTextView).text = parseTime(route.legs.first().locs.first().depTime)
            cardView.findViewById<TextView>(R.id.routeArrivesTextView).text = parseTime(route.legs.last().locs.last().arrTime)

            val legsViewManager = CustomGridLayoutManager(context, LinearLayoutManager.HORIZONTAL)
            legsViewManager.setScrollEnabled(false)
            val legsViewAdapter = RoutesLegCardAdapter(route.legs)
            val legsRecyclerView = cardView.findViewById<RecyclerView>(R.id.routeLegsRecyclerView)
            legsRecyclerView.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                layoutManager = legsViewManager
                adapter = legsViewAdapter
            }
            val infoViewManager = LinearLayoutManager(context)

            val selectedIndicator = cardView.findViewById<ImageView>(R.id.resultSelectedmageView)

            val infoViewAdapter = RoutesInfoCardAdapter(combineLegs(route.legs))
            val infoRecyclerView = cardView.findViewById<RecyclerView>(R.id.routeInfoRecyclerView)
            infoRecyclerView.apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)
                layoutManager = infoViewManager
                adapter = infoViewAdapter
            }
            infoRecyclerView.visibility = View.GONE
            // TODO Cannot set listener directly, pass into items inside it
            infoRecyclerView.setOnClickListener {
                clickListener(route)
            }

            var infoVisible: Boolean = false
            // TODO Change arrow when view is open
            val showMoreButton = cardView.findViewById<ImageButton>(R.id.showRouteButton)
            showMoreButton.setOnClickListener {
                if(!infoVisible) {
                    infoRecyclerView.visibility = View.VISIBLE
                    infoVisible = !infoVisible
                    val caretUp = R.drawable.ic_arrowup_glyph
                    showMoreButton.setImageResource(caretUp)
                }
                else {
                    infoRecyclerView.visibility = View.GONE
                    infoVisible = !infoVisible
                    val caretDown = R.drawable.ic_arrowdown_glyph
                    showMoreButton.setImageResource(caretDown)
                }
            }
            cardView.setOnClickListener {
                clickListener(route)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoutesResultsCardAdapter.RouteResultViewHolder {
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_card, parent, false) as CardView
        return RouteResultViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: RouteResultViewHolder, position: Int) {
        holder.bind(inputData[position], mContext, clickListener)
    }

    override fun getItemCount() = inputData.size
}