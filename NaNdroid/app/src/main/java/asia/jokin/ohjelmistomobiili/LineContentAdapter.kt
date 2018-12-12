package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

class LineContentAdapter (private val inputData: ArrayList<String>, classContext: Context):
        RecyclerView.Adapter<LineContentAdapter.MyViewHolder>() {
    private val appContext: Context = classContext


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineContentAdapter.MyViewHolder {
        // create a new view
        val listView = LayoutInflater.from(parent.context)
                .inflate(R.layout.line_card_content, parent, false) as ConstraintLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(listView)
    }

    override fun onBindViewHolder(holder: LineContentAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val responseData = JSONObject(inputData[position])
        val timeFromStart: TextView = holder.itemView.findViewById(R.id.timeFromStart)
        val lineStopName: TextView = holder.itemView.findViewById(R.id.lineStopName)
        val lineListItem: ConstraintLayout = holder.itemView.findViewById(R.id.lineContentConstraint)

        timeFromStart.text = responseData.getInt("time").toString()
        lineStopName.text = responseData.getString("name")
        Log.e("useful",responseData.toString())
        if (position%2==1){
            lineListItem.setBackgroundColor(holder.itemView.resources.getColor(R.color.lightGrey))
            //lineListItem.setBackgroundColor(R.color.lightGrey)
        }

        lineListItem.setOnClickListener{
            val clickIntent = Intent(appContext, PopupActivity::class.java)
            clickIntent.putExtra("stopid", responseData.getString("code").toInt())
            appContext.startActivity(clickIntent)
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(listView: ConstraintLayout) : RecyclerView.ViewHolder(listView)

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}