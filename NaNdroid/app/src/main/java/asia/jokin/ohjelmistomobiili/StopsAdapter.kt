package asia.jokin.ohjelmistomobiili

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class StopsAdapter (private val inputData: ArrayList<String>, classContext: Context, view: View):
        RecyclerView.Adapter<StopsAdapter.MyViewHolder>() {
    private val appContext: Context = classContext

    private val preferences = PreferenceManager.getDefaultSharedPreferences(classContext)!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopsAdapter.MyViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.stop_card, parent, false) as CardView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: StopsAdapter.MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewManager = LinearLayoutManager(appContext)

        val responseData = JSONObject(inputData[position])
        val departureDataString: String = responseData.getString("departures")
        if (departureDataString!="") {

            val departureData: JSONArray = responseData.getJSONArray("departures")

            val preference: Int = preferences.getString("times_shown","5").toInt()


            val data: ArrayList<String> = ArrayList()
            var maxStops = departureData.length()
            if (maxStops>preference) maxStops = preference

            for (i in (0 until maxStops)) {
                data.add(departureData[i].toString())
            }

            viewAdapter = BusStopAdapter(data,appContext,responseData.getString("code"))
            recyclerView = holder.cardView.findViewById<RecyclerView>(R.id.stopRecycle).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
        holder.cardView.findViewById<TextView>(R.id.lineName).text = responseData.getString("name_fi")

        val favStar: ImageView = holder.cardView.findViewById(R.id.favStar)
        setFavIcon(favStar,responseData.getString("code"))
        favStar.setOnClickListener{
            changeFavStatus(
                    favStar,
                    responseData.getString("code"),
                    responseData.getString("name_fi"),
                    "NOT YET IMPLEMENTED")
        }

        val cardContent:ConstraintLayout = holder.cardView.findViewById(R.id.stopContent)
        cardContent.setOnClickListener {
            val clickIntent = Intent(appContext, MapsActivity::class.java)
            clickIntent.putExtra("stopid", responseData.getString("code").toInt())
            appContext.startActivity(clickIntent)
        }
    }

    @SuppressLint("ApplySharedPref")
    private fun changeFavStatus(favStar: ImageView, itemCode: String, itemName: String, itemLines: String){
        val stopString = preferences.getString("favs_array_stops","[]")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)
            for(i in (0 until stopArray.length())) {
                if (stopArray[i] is JSONObject) {
                    val jsonObject: JSONObject = stopArray[i] as JSONObject
                    if (jsonObject.getString("code") == itemCode) {
                        favStar.setImageResource(R.drawable.ic_star_unselect)
                        stopArray.remove(i)

                        val editor: SharedPreferences.Editor = preferences.edit()
                        editor.putString("favs_array_stops", stopArray.toString())
                        editor.commit()

                        return
                    }
                }
                else {
                    Log.e("changeFavStatus","Unknown setting type")
                }
            }
            val newObject = JSONObject("{'name':'$itemName','code':'$itemCode','lines':'$itemLines'}")
            favStar.setImageResource(R.drawable.ic_star_select)
            stopArray.put(newObject)
            Log.e("changeFavStatus","status added with "+newObject.toString())
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("favs_array_stops", stopArray.toString())
            editor.commit()
        }

    }

    private fun setFavIcon(favStar: ImageView, itemCode: String){
        val stopString = preferences.getString("favs_array_stops","")
        if (stopString != ""){
            val stopArray = JSONArray(stopString)
            for(i in (0 until stopArray.length())) {
                if (stopArray[i] is JSONObject) {
                    val jsonObject: JSONObject = stopArray[i] as JSONObject
                    if (jsonObject.getString("code") == itemCode) {
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
    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = inputData.size
}