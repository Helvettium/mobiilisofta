package asia.jokin.ohjelmistomobiili

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng

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
    //used with FetchDataSingleton.getInstance(this.applicationContext).doStuff()

    var context: Context
    private val BASE_URL: String = "http://api.publictransport.tampere.fi/prod/?user=zx123&pass=qmF:L}h3wR2n"

    init {
        // Init using context argument
        this.context = context
    }

    fun hello() {
        Toast.makeText(context, "Hello",
                Toast.LENGTH_SHORT).show();
    }

    fun getStopsData(latlng: LatLng? = LatLng(0.0, 0.0)){
        val queue = Volley.newRequestQueue(context)

        var lat = latlng.toString()
        lat = lat.substringAfter("(")
        lat = lat.substring(0,8)
        lat = lat.replace(".", "")

        var lng = latlng.toString()
        lng = lng.substringAfter(",")
        lng = lng.substring(0,8)
        lng = lng.replace(".", "")


        val requestUrl = "$BASE_URL&request=stops_area&center_coordinate=$lng,$lat&diameter=1000"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, requestUrl, null,
                Response.Listener { response ->
                    // Display the response in a toast
                     Toast.makeText(context, response.toString(),
                            Toast.LENGTH_LONG).show();
                },
                Response.ErrorListener { response -> Toast.makeText(context, response.toString(),
                        Toast.LENGTH_LONG).show(); })

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    //TODO muut data haut

    companion object : SingletonHolder<FetchDataSingleton, Context>(::FetchDataSingleton)
}