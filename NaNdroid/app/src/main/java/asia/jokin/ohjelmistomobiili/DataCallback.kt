package asia.jokin.ohjelmistomobiili

import android.content.Context
import org.json.JSONArray

interface DataCallback {
    fun onSuccess(response: JSONArray, context: Context)
}