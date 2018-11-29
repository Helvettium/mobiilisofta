package asia.jokin.ohjelmistomobiili

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class AlertsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var fetchManager = FetchDataSingleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)
        viewManager = LinearLayoutManager(this)

        fetchManager.getInstance(this.applicationContext).getGeneralMessages(object: AlertDataCallback {
            override fun onSuccess(response: ArrayList<Alert>, context: Context) {
                val fetchedData: ArrayList<Alert> = response

                viewAdapter = AlertCardAdapter(fetchedData)

                recyclerView = findViewById<RecyclerView>(R.id.alertRecycle).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
            }
        })
    }
}
