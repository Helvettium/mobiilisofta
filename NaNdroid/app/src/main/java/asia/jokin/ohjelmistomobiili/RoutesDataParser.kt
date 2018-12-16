package asia.jokin.ohjelmistomobiili

import android.content.Context
import com.google.gson.Gson
import java.io.Serializable
import com.google.gson.reflect.TypeToken

interface RoutesDataCallback {
    fun onSuccess(response: List<List<Route>>, context: Context)
}

interface LocationDataCallback {
    fun onSuccess(response: List<Point>, context: Context)
}

data class Route(val length: Double, val duration: Long, val legs: List<Leg>): Serializable
data class Leg(val length: Double, val duration: Double, val type: String, val code: String? = null, val locs: List<Location>): Serializable
data class Location(val coord: Coordinate, val arrTime: String, val depTime: String, val name: String? = null): Serializable
data class Coordinate(val x: Double, val y: Double): Serializable

data class Point(val locType: String, val name: String, val coords: String, val details: Detail? = null): Serializable
data class Detail(val code: String): Serializable

class RoutesDataParser {
    companion object {
        fun parseRouteResultData(data: String): List<List<Route>> {
            // GSON magic to rip the response array out of the response array
            val routes = Gson().fromJson<List<List<Route>>>(data, object : TypeToken<List<List<Route>>>() {}.type)
            return routes
        }

        fun parseLocationResultData(data: String): List<Point> {
            return Gson().fromJson(data, Array<Point>::class.java).toList()
        }
    }
}

