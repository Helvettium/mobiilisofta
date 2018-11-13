/*
data class StopsData(val data1: String)
data class LineData(val data1: String)
data class StopData(val data1: String)

@SuppressLint("StaticFieldLeak")
object FetchDataSingleton{

    private val appContext: Context = FetchDataSingleton

    private val BASE_URL: String = "http://api.publictransport.tampere.fi/prod/?user=zx123&pass=qmF:L}h3wR2n"
    private var endUrl: String = ""
    val stopData: ArrayList<StopData> = ArrayList()
    private val stopsData = ArrayList<StopsData>()
    private val lineData = ArrayList<LineData>()
    //private lateinit var appContext: Context
    // TODO IMPLEMENTOI
    // LocationSingleton.checkPermissionForLocation

    fun getStopsData(locationLatitude: String? = null, locationLongitude: String? = null){

    }

    fun getLineData(locationLatitude: String? = null, locationLongitude: String? = null){

    }

    fun getStopData(locationName: String){
        val searchTerm = "&code=$locationName"
        fetchData(searchTerm)
    }

    private fun fetchData(searchTerm: String){
        val queue = Volley.newRequestQueue(this)
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(Request.Method.GET, BASE_URL+searchTerm,
                Response.Listener { response -> getJson(response) }, Response.ErrorListener { error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
    private fun getJson(response: String) {
        // TODO move elsewhere
        stopData.clear()
        stopData.add(StopData(response))
    }
}
*/