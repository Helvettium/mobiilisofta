package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class PopupFragment : Fragment() {
    private val mDepartures: MutableList<PopupData> = mutableListOf()
    private lateinit var mTitleText: TextView
    private lateinit var mTitleStar: ImageView
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
        mTitleStar = aView.findViewById(R.id.popupStar)
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

                // Helpommin käsiteltävään muotoon
                val jsonObject = response.getJSONObject(0)

                // Otsikko
                mTitleText.text = jsonObject.get("name_fi").toString()

                // Tähti
                if (isFavourite(jsonObject.getString("code").toInt())) {
                    mTitleStar.setImageResource(R.drawable.ic_star_select)
                }
                else {
                    mTitleStar.setImageResource(R.drawable.ic_star_white)
                }

                // Tähden listener
                mTitleStar.setOnClickListener {
                    toggleFavourite(jsonObject)
                }

                // Lähtevät vuorot, jos rajapinta palautti
                val departures = jsonObject.get("departures")

                if (departures is JSONArray) {
                    for(i in 0 until departures.length()) {
                        val json = departures.getJSONObject(i)
                        val code = json.getString("code")
                        val date = json.getString("date")
                        val name = json.getString("name1")
                        val time = json.getString("time").substring(0, 2) + ":" + json.getString("time").substring(2, 4)
                        val item = PopupData(code, name, time, date)

                        mDepartures.add(item)
                    }
                }
                else {
                    // Ei lähteviä linjoja -viesti?
                }

                // Tökitään adapteria että se varmasti päivittyy
                mViewAdapter.notifyDataSetChanged()
            }
        })
    }

    fun isFavourite(aCode: Int): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val stopString = preferences.getString("favs_array_stops", "[]")
        val stopCode = aCode.toString()

        if (stopString != "") {
            val stopArray = JSONArray(stopString)

            for (i in (0 until stopArray.length())) {
                if (stopArray[i] is JSONObject) {
                    val stopObject = stopArray[i] as JSONObject
                    if (stopObject.getString("code") == stopCode) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun toggleFavourite(aJsonObject: JSONObject) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val stopString = preferences.getString("favs_array_stops","[]")
        val stopCode = aJsonObject.getString("code")
        val editor: SharedPreferences.Editor

        if (stopString != "") {
            val stopArray = JSONArray(stopString)

            for(i in (0 until stopArray.length())) {
                if (stopArray[i] is JSONObject) {
                    val jsonObject: JSONObject = stopArray[i] as JSONObject
                    if (jsonObject.getString("code") == stopCode) {

                        // Tähti
                        mTitleStar.setImageResource(R.drawable.ic_star_white)

                        // Poistetaan
                        stopArray.remove(i)
                        editor = preferences.edit()
                        editor.putString("favs_array_stops", stopArray.toString())
                        editor.apply()

                        return
                    }
                }
            }

            // Tähti
            mTitleStar.setImageResource(R.drawable.ic_star_select)

            // Listaan menevä tieto featuring typerä purkka koska määrittelyssä ei oltu tarpeeksi tarkkoja
            val newFavourite = JSONObject()
                newFavourite.put("code", aJsonObject.getString("code").toInt().toString())
                newFavourite.put("name", aJsonObject.getString("name_fi"))
                newFavourite.put("lines", aJsonObject.getString("lines"))

            // Lisätään
            stopArray.put(newFavourite)
            editor = preferences.edit()
            editor.putString("favs_array_stops", stopArray.toString())
            editor.apply()
        }
    }
}