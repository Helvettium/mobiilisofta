package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

interface RoutesDataCallback {
    fun onSuccess(response: List<Route>, context: Context)
    fun onFailure(error: String, context: Context)
}

interface LocationDataCallback {
    fun onSuccess(response: List<Point>, context: Context)
    fun onFailure(error: String, context: Context)
}

data class RoutesDataRequest(val from: String, val to: String, val Date: String, val Time: String, val timeType: String)
data class GeocodeDataRequest(val key: String)

@Parcelize
data class Route(val length: Double, val duration: Long, val legs: List<Leg>): Parcelable
@Parcelize
data class Leg(val length: Double, val duration: Double, val type: String, val code: String? = null, val locs: List<Location>): Parcelable
@Parcelize
data class Location(val coord: Coordinate, val arrTime: String, val depTime: String, val name: String? = null): Parcelable
@Parcelize
data class Coordinate(val x: Double, val y: Double): Parcelable


@Parcelize
data class Point(val locType: String, val name: String, val coords: String, val details: Detail? = null): Parcelable
@Parcelize
data class Detail(val code: String): Parcelable

class RoutesDataParser {
    companion object {
        fun parseRouteResultData(data: String): List<Route> {
            // GSON magic to rip the response array out of the response array
            //val routes = Gson().fromJson<List<List<Route>>>(data, object : TypeToken<List<List<Route>>>() {}.type)
            val rawroutes = Gson().fromJson(data, Array<Array<Route>>::class.java).toList()
            val routes = ArrayList<Route>()
            for(Array in rawroutes) {
                routes.add(Array[0])
            }
            println(routes)
            return routes
        }

        fun parseLocationResultData(data: String): List<Point> {
            return when(data.isEmpty()) {
                true -> ArrayList<Point>().toList()
                false -> Gson().fromJson(data, Array<Point>::class.java).toList()
            }
        }
    }
}

