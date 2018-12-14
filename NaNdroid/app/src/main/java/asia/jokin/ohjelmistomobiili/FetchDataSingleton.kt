package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import org.json.JSONArray
import java.util.*

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}

class FetchDataSingleton private constructor(context: Context) {
    private var context: Context = context
    private val queue = Volley.newRequestQueue(context)
    private val BASE_URL: String = "http://api.publictransport.tampere.fi/prod/?user=4yu97&pass=h3zwf&&epsg_in=4326&epsg_out=4326"
    private val SIRI_URL: String = "http://data.itsfactory.fi/siriaccess"
    private var lastCoord = LatLng(0.0,0.0) // variable for the last getStopsData call coordinates
    private lateinit var stopsResponse: JSONArray // variable for the last getStopsData call response
    private var lastStop = -1 // variable for the last getStopData call stopcode
    private var stopTime = Calendar.getInstance() // variable for the last getClosestStops call time
    private lateinit var stopResponse: JSONArray // variable for the last getStopData call response
    private var lastLine = "" // variable for the last getLineData call Line
    private lateinit var lineResponse: JSONArray // variable for the last getLineData call response
    private var closestTime = Calendar.getInstance() // variable for the last getClosestStops call time
    private lateinit var closestResponse: JSONArray // variable for the last getClosestStops call response

    init {
        // Init using context argument
        this.closestTime.add(Calendar.YEAR, -1)
        this.stopTime.add(Calendar.YEAR, -1)
    }

    fun getStopsData(latlng: LatLng = LatLng(61.4980000, 23.7604000), dia: Int = 1000, callback: DataCallback){
        /*
        USAGE:
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(testLocation, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        if (SphericalUtil.computeDistanceBetween(latlng, lastCoord) > 100){
            var lat = latlng.toString()
            lat = lat.substringAfterLast("(", ")")
            lat = lat.dropLast(1)
            val lng = lat.substringAfter(",")
            lat = lat.substringBefore(",")
            val diameter = dia.toString()

            val requestUrl = "$BASE_URL&request=stops_area&diameter=$diameter&p=1101&center_coordinate=$lng,$lat"

            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                    Response.Listener { response ->
                        lastCoord = latlng
                        stopsResponse = response
                        callback.onSuccess(response, context)
                    },
                    Response.ErrorListener { response ->
                        System.err.println(response)
                    })

            // Add the request to the RequestQueue.
            queue.add(jsonArrayRequest)
        }
        else {
            callback.onSuccess(stopsResponse, context)
        }
    }

    fun getStopData(stopcode: Int, callback: DataCallback){
        /*
        USAGE:
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(stopcode, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        val now = Calendar.getInstance()
        now.add(Calendar.MINUTE, -1)
        if (stopcode == lastStop && now.after(stopTime) || stopcode != lastStop) {
            val requestUrl = "$BASE_URL&request=stop&p=10101011001&code=$stopcode"

            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                    Response.Listener { response ->
                        lastStop = stopcode
                        stopResponse = response
                        stopTime = Calendar.getInstance()
                        callback.onSuccess(response, context)
                    },
                    Response.ErrorListener { response ->
                        System.err.println(response)
                    })

            // Add the request to the RequestQueue.
            queue.add(jsonArrayRequest)
        }
        else {
            callback.onSuccess(stopResponse, context)
        }
    }

    fun getLineData(line: String, callback: DataCallback){
        /*
        USAGE:
        FetchDataSingleton.getInstance(this.applicationContext).getStopsData(line, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        if (line != lastLine) {
            val requestUrl = "$BASE_URL&request=lines&p=100111011&query=$line"

            val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                    Response.Listener { response ->
                        lastLine = line
                        lineResponse = response
                        callback.onSuccess(response, context)
                    },
                    Response.ErrorListener { response ->
                        System.err.println(response)
                    })

            // Add the request to the RequestQueue.
            queue.add(jsonArrayRequest)
        }
        else {
            callback.onSuccess(lineResponse, context)
        }
    }

    fun getClosestStops(latlng: LatLng = LatLng(61.4980000, 23.7604000), numberOfStops: Int = 5, callback: DataCallback) {
        /*
        USAGE:
        FetchDataSingleton.getInstance(this.applicationContext).getFiveClosestStops(testLocation, 5, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        val now = Calendar.getInstance()
        // now.add(Calendar.MINUTE, -1)
        if (now.after(closestTime)) {
            val stopsData = JSONArray() //JSONArray for the return value

            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            FetchDataSingleton.getInstance(context).getStopsData(latlng, 400, object: DataCallback{ //make a getStopsData call
                override fun onSuccess(stops: JSONArray, context: Context) {
                    for (i in 0 until stops.length()) { //loop through the stops in response
                        val item = stops.getJSONObject(i) //get JSONObject in position i
                        val stopcode = item.get("code").toString().toInt() //get code value from item
                        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
                        FetchDataSingleton.getInstance(context).getStopData(stopcode, object : DataCallback { //make a getStopData call for given stopcode
                            override fun onSuccess(stop: JSONArray, context: Context) {
                                if (stop.getJSONObject(0).get("departures")!="")
                                    stopsData.put(stop.getJSONObject(0)) //put the received data to a JSONArray
                                if (stopsData.length() == numberOfStops){
                                    closestResponse = stopsData
                                    closestTime = Calendar.getInstance()
                                    callback.onSuccess(stopsData, context) //call callback when there are 5 objects in array
                                }
                            }
                        })
                    }
                }
            })
        }
        else {
            callback.onSuccess(closestResponse, context) //call callback when there are 5 objects in array
        }
    }

    fun getGeneralMessages(callback: AlertDataCallback) {
        val requestUrl = "$SIRI_URL/gm/json"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, requestUrl, null,
                Response.Listener { response ->
                    callback.onSuccess(parseData(response), context)
                },
                Response.ErrorListener { response ->
                    System.err.println("Error in retrieving general messages $response")
                })

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    companion object : SingletonHolder<FetchDataSingleton, Context>(::FetchDataSingleton)
}