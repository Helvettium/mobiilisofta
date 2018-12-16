package asia.jokin.ohjelmistomobiili

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.routes_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class RoutesFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private val stops = arrayOf("Yksi", "kaksi", "Kolme")
    private var mTime: Calendar = Calendar.getInstance()
    private val mTimeFormatter = SimpleDateFormat("HH:mm", Locale("fi"))
    private val mDateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale("fi"))
    private val mRequestTimeFormatter = SimpleDateFormat("HHmm", Locale("fi"))
    private val mRequestDateFormatter = SimpleDateFormat("yyyyMMdd", Locale("fi"))
    private lateinit var timeEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var departureRadioButton: RadioButton
    private lateinit var routesOriginEditText: EditText
    private lateinit var routesDestinationEditText: EditText

    private var mOriginCoords: String = ""
    private var mDestinationCoords: String = ""

    enum class LocationType {
        ORIGIN, DESTINATION
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.routes_fragment, container, false)
        val routesOriginTextInput = view.findViewById<EditText>(R.id.routesOriginTextInput)

        this.timeEditText = view.findViewById(R.id.timeEditText)
        timeEditText.setText(mTimeFormatter.format(mTime.time))
        this.dateEditText = view.findViewById(R.id.dateEditText)
        dateEditText.setText(mDateFormatter.format(mTime.time))

        val routesOriginAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.routesOriginTextInput)
        ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, stops).also { adapter -> routesOriginAutoComplete.setAdapter(adapter)}

        timeEditText.setOnClickListener {
            val tpd = TimePickerDialog.newInstance(
                    this,
                    mTime.get(Calendar.HOUR_OF_DAY),
                    mTime.get(Calendar.MINUTE),
                    true
            )
            tpd.show(this.activity?.fragmentManager, "TimePickerDialog")
        }

        dateEditText.setOnClickListener {
            val dpd = DatePickerDialog.newInstance(
                    this,
                    mTime.get(Calendar.YEAR),
                    mTime.get(Calendar.MONTH),
                    mTime.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show(this.activity?.fragmentManager, "DatePickerDialog")
        }

        departureRadioButton = view.findViewById(R.id.routeOnDepartureTimeRadioButton)
        departureRadioButton.isChecked = true

        routesOriginEditText = view.findViewById(R.id.routesOriginTextInput)
        routesOriginEditText.setOnClickListener { startActivityForResult(Intent(this.context, RouteLocationPickerActivity::class.java), LocationType.ORIGIN.ordinal) }

        routesDestinationEditText = view.findViewById(R.id.routesDestinationTextInput)
        routesDestinationEditText.setOnClickListener { startActivityForResult(Intent(this.context, RouteLocationPickerActivity::class.java), LocationType.DESTINATION.ordinal) }

        val searchRoutesButton = view.findViewById<Button>(R.id.routesSearchRouteButton)
        searchRoutesButton.setOnClickListener { v ->
            view.findViewById<ProgressBar>(R.id.routesLoadingIndicator).visibility = View.VISIBLE
            val timeType = if(departureRadioButton.isChecked) {
                "departure"
            } else {
                "arrival"
            }
            val request = RoutesDataRequest(
                    mOriginCoords,
                    mDestinationCoords,
                    mRequestDateFormatter.format(mTime.time),
                    mRequestTimeFormatter.format(mTime.time),
                    timeType
            )
            FetchDataSingleton.getInstance(v.context).getRouteResults(request, object: RoutesDataCallback {
            override fun onSuccess(response: List<Route>, context: Context) {
                view.findViewById<ProgressBar>(R.id.routesLoadingIndicator).visibility = View.INVISIBLE
                if(response.isEmpty()) {
                    view.findViewById<ProgressBar>(R.id.routesLoadingIndicator).visibility = View.INVISIBLE
                    val noAlertsSnackbar = Snackbar.make(view.findViewById(R.id.coordinatorLayout), "No routes found", Snackbar.LENGTH_SHORT)
                    noAlertsSnackbar.show()
                } else {
                    val fetchedData: List<Route> = response
                    val startupIntent = Intent(v.context, RouteResultsActivity::class.java)
                    startupIntent.putParcelableArrayListExtra("resultsData", fetchedData as ArrayList<Route>)
                    startActivity(startupIntent)
                }

            }
            override fun onFailure(error: String, context: Context) {
                view.findViewById<ProgressBar>(R.id.routesLoadingIndicator).visibility = View.INVISIBLE
                val noAlertsSnackbar = Snackbar.make(view.findViewById(R.id.coordinatorLayout), error, Snackbar.LENGTH_SHORT)
                noAlertsSnackbar.show()
            }
            })
        }

        return view
    }

    override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
        mTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        mTime.set(Calendar.MINUTE, minute)
        timeEditText.setText(mTimeFormatter.format(mTime.time))
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        mTime.set(Calendar.YEAR, year)
        mTime.set(Calendar.MONTH, monthOfYear)
        mTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        dateEditText.setText(mDateFormatter.format(mTime.time))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LocationType.ORIGIN.ordinal) {
            if (resultCode == RESULT_OK) {
                routesOriginEditText.setText(data?.getStringExtra("locationName"))
                mOriginCoords = data?.getStringExtra("coords").toString()
            }
        } else if (requestCode == LocationType.DESTINATION.ordinal) {
            if (resultCode == RESULT_OK) {
                println("saatu kohde")
                println(data?.getStringExtra("locationName"))
                routesDestinationEditText.setText(data?.getStringExtra("locationName"))
                mDestinationCoords = data?.getStringExtra("coords").toString()
            }
        }
    }
}