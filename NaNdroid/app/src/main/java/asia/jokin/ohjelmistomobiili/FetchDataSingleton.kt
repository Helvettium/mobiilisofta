package asia.jokin.ohjelmistomobiili

data class StopsData(val data1: String)
data class LineData(val data1: String)
data class StopData(val data1: String)

object FetchDataSingleton {
    private val BASE_URL: String = "http://api.publictransport.tampere.fi/prod/?user=zx123&pass=qmF:L}h3wR2n"
    private var endUrl: String = ""
    // TODO IMPLEMENTOI
    // LocationSingleton.checkPermissionForLocation

    fun getStopsData(locationLatitude: String? = null, locationLongitude: String? = null){

    }

    fun getLineData(locationLatitude: String? = null, locationLongitude: String? = null){

    }

    fun getStopData(locationLatitude: String? = null, locationLongitude: String? = null){

    }

    fun updateUrl(searchTerm: String = "&key=met"){

    }

    fun fetchData(locationLatitude: String? = null, locationLongitude: String? = null){

    }
}
