package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray

class PopupFragment : Fragment() {
    private val mDepartures: MutableList<PopupDeparture> = mutableListOf()
    private lateinit var mTitleText: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewManager: RecyclerView.LayoutManager

    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        return aInflater.inflate(R.layout.popup_fragment, aContainer, false)
    }

    override fun onViewCreated(aView: View, aSavedInstanceState: Bundle?) {
        super.onViewCreated(aView, aSavedInstanceState)

        mViewAdapter = PopupAdapter(mDepartures)
        mViewManager = LinearLayoutManager(activity)
        mTitleText = aView.findViewById(R.id.popupTitle)
        mRecyclerView = aView.findViewById<RecyclerView>(R.id.popupRCV).apply {
            setHasFixedSize(true)
            layoutManager = mViewManager
            adapter = mViewAdapter
        }
    }

    fun updatePopup(aStopCode: Int, aApplicationContext: Context) {
        // Haetaan pysäkin tiedot
        FetchDataSingleton.getInstance(aApplicationContext).getStopData(aStopCode, object: DataCallback {
            override fun onSuccess(response: JSONArray, context: Context) {
                // Tyhjennetään lista
                mDepartures.clear()

                // Pysäkin nimi
                val jsonObject = response.getJSONObject(0)
                mTitleText.text = jsonObject.get("name_fi").toString()

                // Lähtevät vuorot, jos rajapinta palautti
                val departures = jsonObject.get("departures")

                if (departures is JSONArray) {
                    for(i in 0 until departures.length()) {
                        val json = departures.getJSONObject(i)
                        val code = json.getString("code")
                        val date = json.getString("date")
                        val name = json.getString("name1")
                        val time = json.getString("time").substring(0, 2) + ":" + json.getString("time").substring(2, 4)
                        val item = PopupDeparture(code, name, time, date)

                        mDepartures.add(item)
                    }
                }
                else {
                    // Ei lähteviä linjoja -viesti?
                }

                // Tökitään adapteria että se varmasti päivittyy
                mViewAdapter.notifyDataSetChanged()

                // Debug
                //Log.d("JSON", jsonObject.toString())
            }
        })
    }

    fun isFavourite(aCode: String) {

    }

    fun toggleFavourite(aCode: String) {

    }
}