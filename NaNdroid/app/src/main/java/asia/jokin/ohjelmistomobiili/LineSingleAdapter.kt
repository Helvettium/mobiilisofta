package asia.jokin.ohjelmistomobiili

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class LineSingleAdapter (private val inputData: ArrayList<String>, classContext: Context):
        RecyclerView.Adapter<LineSingleAdapter.MyViewHolder>() {
    private val appContext: Context = classContext

    private val preferences = PreferenceManager.getDefaultSharedPreferences(classContext)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineSingleAdapter.MyViewHolder {
        // create a new view
        val listView = LayoutInflater.from(parent.context)
                .inflate(R.layout.line_card_single, parent, false) as ConstraintLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(listView)
    }

    override fun onBindViewHolder(holder: LineSingleAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val responseData = JSONObject(inputData[position])
        val busNr: TextView = holder.itemView.findViewById(R.id.busNr)
        val lineFromTo: TextView = holder.itemView.findViewById(R.id.lineFromTo)
        val lineListItem: ConstraintLayout = holder.itemView.findViewById(R.id.lineSingleConstraint)
        val favStar: ImageView = holder.itemView.findViewById(R.id.favStar)

        setFavIcon(favStar,responseData.getString("name"))

        busNr.text = responseData.getString("code")
        lineFromTo.text = responseData.getString("name")
        Log.e("useful",responseData.toString())
        if (position%2==1){
            lineListItem.setBackgroundColor(holder.itemView.resources.getColor(R.color.lightGrey))
        }

        favStar.setOnClickListener {
            changeFavStatus(
                    favStar,
                    responseData.getString("code"),
                    responseData.getString("name"),
                    responseData.getJSONArray("line_stops").toString())
        }

        lineListItem.setOnClickListener{
            val clickIntent = Intent(appContext, LinePopup::class.java)
            clickIntent.putExtra("stopcontent", responseData.getJSONArray("line_stops").toString())
            appContext.startActivity(clickIntent)
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun changeFavStatus(favStar: ImageView, itemCode: String, itemName: String, itemLineStops: String){
        val stopString = preferences.getString("favs_array_lines","[]")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)
            for(i in (0 until stopArray.length())) {
                if (stopArray[i] is JSONObject) {
                    val jsonObject: JSONObject = stopArray[i] as JSONObject
                    if (jsonObject.getString("name") == itemName) {
                        favStar.setImageResource(R.drawable.ic_star_unselect)
                        stopArray.remove(i)

                        val editor: SharedPreferences.Editor = preferences.edit()
                        editor.putString("favs_array_lines", stopArray.toString())
                        editor.commit()

                        return
                    }
                }
                else {
                    Log.e("changeFavStatus","Unknown setting type")
                }
            }
            val newObject = JSONObject("{'name':'$itemName','code':'$itemCode','line_stops':'$itemLineStops'}")
            favStar.setImageResource(R.drawable.ic_star_select)
            stopArray.put(newObject)
            Log.e("changeFavStatus","status added with "+newObject.toString())
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("favs_array_lines", stopArray.toString())
            editor.commit()
        }

    }

    private fun setFavIcon(favStar: ImageView, itemName: String){
        val stopString = preferences.getString("favs_array_lines","")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)
            for(i in (0 until stopArray.length())) {
                if (stopArray[i] is JSONObject) {
                    val jsonObject: JSONObject = stopArray[i] as JSONObject
                    if (jsonObject.getString("name") == itemName) {
                        favStar.setImageResource(R.drawable.ic_star_select)
                        return
                    }
                }
                else {
                    Log.e("setFavIcon","Unknown setting type")
                }
            }
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