package asia.jokin.ohjelmistomobiili

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PopupAdapter(private val aDepartures: MutableList<PopupDeparture>): RecyclerView.Adapter<PopupAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        // Datan koko, adapterin vaatimuksia
        return aDepartures.size
    }

    override fun onCreateViewHolder(aParent: ViewGroup, aViewType: Int): ViewHolder {
        // Yhdistetään layout ja viewholder
        return ViewHolder(LayoutInflater.from(aParent.context).inflate(R.layout.popup_item, aParent, false))
    }

    override fun onBindViewHolder(aHolder: ViewHolder, aPosition: Int) {
        val item = aDepartures[aPosition]

        aHolder.time.text = item.time
        aHolder.name.text = item.name
    }

    class ViewHolder (aView: View) : RecyclerView.ViewHolder(aView) {
        val time = aView.findViewById<TextView>(R.id.departureTime)!!
        val name = aView.findViewById<TextView>(R.id.departureName)!!
    }
}