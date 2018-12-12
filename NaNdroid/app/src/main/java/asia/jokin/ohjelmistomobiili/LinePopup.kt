package asia.jokin.ohjelmistomobiili

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class LinePopup : AppCompatActivity() {
    //private var testContent: TextView = findViewById(R.id.testContent)
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_popup)

        viewManager = LinearLayoutManager(this)

        val receivedData: String = intent.getStringExtra("stopcontent")

        val stopList = JSONArray(receivedData)

        val fetchedData: ArrayList<String> = ArrayList()

        for (i in (0 until stopList.length())) {

            fetchedData.add(stopList[i].toString())
        }
        viewAdapter = LineContentAdapter(fetchedData,this.applicationContext)
        recyclerView = findViewById<RecyclerView>(R.id.lineListRec).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
