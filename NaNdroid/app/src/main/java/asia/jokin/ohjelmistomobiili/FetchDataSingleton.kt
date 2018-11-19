package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray

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
    private var context: Context
    private val queue = Volley.newRequestQueue(context)
    private val BASE_URL: String = "http://api.publictransport.tampere.fi/prod/?user=zx123&pass=qmF:L}h3wR2n&&epsg_in=4326&epsg_out=4326"
    private var data: JSONArray = JSONArray()

    init {
        // Init using context argument
        this.context = context
    }

    fun getStopsData(latlng: LatLng? = LatLng(61.4980000, 23.7604000), callback: DataCallback){
        /*
        USAGE:
        fetchManager.getInstance(this.applicationContext).getStopsData(testLocation, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        var lat = latlng.toString()
        lat = lat.substringAfterLast("(", ")")
        lat = lat.dropLast(1)
        var lng = lat.substringAfter(",")
        lat = lat.substringBefore(",")

        val requestUrl = "$BASE_URL&request=stops_area&diameter=1000&p=1101&center_coordinate=$lng,$lat"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                Response.Listener { response ->
                    callback.onSuccess(response, context)
                },
                Response.ErrorListener { response ->
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                })

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    fun getStopData(stopcode: Int, callback: DataCallback){
        /*
        USAGE:
        fetchManager.getInstance(this.applicationContext).getStopsData(stopcode, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        val requestUrl = "$BASE_URL&request=stop&p=10101010001&code=$stopcode"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                Response.Listener { response ->
                    callback.onSuccess(response, context)
                },
                Response.ErrorListener { response ->
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                })

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    fun getLineData(line: String, callback: DataCallback){
        /*
        USAGE:
        fetchManager.getInstance(this.applicationContext).getStopsData(line, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
            }
        })
        */
        val requestUrl = "$BASE_URL&request=lines&p=100111011&query=$line"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                Response.Listener { response ->
                    callback.onSuccess(response, context)
                },
                Response.ErrorListener { response ->
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                })

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    //TODO muut data haut

    companion object : SingletonHolder<FetchDataSingleton, Context>(::FetchDataSingleton)
}