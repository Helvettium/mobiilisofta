package asia.jokin.ohjelmistomobiili

import android.content.Context
import com.google.gson.Gson
import java.io.Serializable
import com.google.gson.reflect.TypeToken



interface RoutesDataCallback {
    fun onSuccess(response: ArrayList<Route>, context: Context)
}


data class Route(val length: Double, val duration: Long, val legs: List<Leg>): Serializable
data class Leg(val length: Double, val duration: Double, val type: String, val locs: List<Location>): Serializable
data class Location(val coord: Coordinate, val arrTime: String, val depTime: String, val name: String): Serializable
data class Coordinate(val x: Double, val y: Double): Serializable

class RoutesDataParser {
    companion object {
        fun parseRouteResultData(data: String): ArrayList<Route> {
            val routes = Gson().fromJson<List<List<Route>>>(data, object : TypeToken<List<List<Route>>>() {}.type)
            println(routes)
            return ArrayList()
        }
    }
}

