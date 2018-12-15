package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class PopupFragment : Fragment() {
    private lateinit var mTitleText: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewManager: RecyclerView.LayoutManager

    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        return aInflater.inflate(R.layout.popup_fragment, aContainer, false)
    }

    override fun onViewCreated(aView: View, aSavedInstanceState: Bundle?) {
        super.onViewCreated(aView, aSavedInstanceState)

        mTitleText = aView.findViewById(R.id.popupTitle)
    }

    fun showStop(aStopCode: Int, aApplicationContext: Context) {

        // Haetaan pysäkin tiedot
        FetchDataSingleton.getInstance(aApplicationContext).getStopData(aStopCode, object: DataCallback {
            override fun onSuccess(response: JSONArray, context: Context) {
                val stopData = response.getJSONObject(0)
                mTitleText.text = stopData.get("name_fi").toString()


                Log.d("JSONArray", response.toString())

                // Do stuff
                /*
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
                */
            }
        })

        // Debug
        Log.d("Marker", aStopCode.toString())

        //Toast.makeText(this, "click", Toast.LENGTH_LONG).show()

        // Vitun raivostuttava tapa viitata ID
        // mTitleText.text = aStopCode
    }
}