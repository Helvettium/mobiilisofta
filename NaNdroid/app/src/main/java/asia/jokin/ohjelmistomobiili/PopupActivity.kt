package asia.jokin.ohjelmistomobiili

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_popup.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class PopupActivity : AppCompatActivity() {
    private val exampleData1: ArrayList<String> = arrayListOf("example")
    private val exampleData2: ArrayList<String> = arrayListOf("00:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        val favStar: ImageView = findViewById(R.id.favStar)

        // Creates a vertical Layout Manager
        rcv_busses.layoutManager = LinearLayoutManager(this)
        val txtName: TextView = findViewById(R.id.txtName) //stop name text view
        //rcv_busses.adapter = PopupAdapter(exampleData1, exampleData2,this)

        val stopcode = intent.getIntExtra("stopid",0)
        // val stopcode = "0035" // Keskustori C

        setFavIcon(favStar,stopcode.toString())

        FetchDataSingleton.getInstance(this.applicationContext).getStopData(stopcode, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
                val lines: ArrayList<String> = ArrayList()
                val arrivals: ArrayList<String> = ArrayList()
                val departures: JSONArray
                val data = response.getJSONObject(0)
                txtName.text = data.get("name_fi").toString()
                departures = if (data.get("departures") != "")
                    data.getJSONArray("departures")
                else
                    JSONArray()

                val timeFormat = SimpleDateFormat("kk:mm")
                val currenttime = timeFormat.format(Date())

                favStar.setOnClickListener {
                    changeFavStatus(favStar,
                            stopcode.toString(),
                            data.getString("name_fi"),
                            "")
                }

                for (i in 0 until departures.length()) {
                    val departure = departures.getJSONObject(i)
                    lines.add("${departure.get("code")} ${departure.get("name1")}")
                    var arrival = departure.get("time").toString()
                    arrival = "${arrival.substring(0,2)}:${arrival.substring(2,4)}"
                    arrivals.add(arrival)
                }
                addData(lines, arrivals)
            }
        })
    }

    @SuppressLint("ApplySharedPref")
    private fun changeFavStatus(favStar: ImageView, itemCode: String, itemName: String, itemLines: String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)!!
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
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)!!
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

    fun addData(lines: ArrayList<String>, arrivals: ArrayList<String>) {
        //rcv_busses.adapter = PopupAdapter(lines, arrivals, this)
    }
}
