package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.format.Time
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_popup.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class PopupActivity : AppCompatActivity() {
    val exampleData1: ArrayList<String> = arrayListOf("example")
    val exampleData2: ArrayList<String> = arrayListOf("00:00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        // Creates a vertical Layout Manager
        rcv_busses.layoutManager = LinearLayoutManager(this)
        val txtName: TextView = findViewById(R.id.txtName) //stop name text view
        rcv_busses.adapter = PopupAdapter(exampleData1, exampleData2,this)

        val stopcode = intent.getIntExtra("stopid",0)
        // val stopcode = "0035" // Keskustori C

        FetchDataSingleton.getInstance(this.applicationContext).getStopData(stopcode, object: DataCallback{
            override fun onSuccess(response: JSONArray, context: Context) {
                // Do stuff
                var lines: ArrayList<String> = ArrayList()
                var arrivals: ArrayList<String> = ArrayList()
                var departures: JSONArray
                val data = response.getJSONObject(0)
                txtName.text = data.get("name_fi").toString()
                if (data.get("departures") != "")
                    departures = data.getJSONArray("departures")
                else
                    departures = JSONArray()

                val timeFormat = SimpleDateFormat("kk:mm")
                val currenttime = timeFormat.format(Date())


                for (i in 0..departures.length()-1) {
                    val departure = departures.getJSONObject(i)
                    lines.add("${departure.get("code")} ${departure.get("name1")}")
                    var arrival = departure.get("time").toString()
                    arrival = "${arrival.substring(0,2)}:${arrival.substring(2,4)}"
                    arrivals.add("$arrival")
                }
                addData(lines, arrivals)
            }
        })
    }

    fun addData(lines: ArrayList<String>, arrivals: ArrayList<String>) {
        rcv_busses.adapter = PopupAdapter(lines, arrivals, this)
    }
}
