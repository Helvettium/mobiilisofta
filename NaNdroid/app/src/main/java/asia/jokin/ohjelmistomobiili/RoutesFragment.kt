package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*


class RoutesFragment : Fragment(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private val stops = arrayOf("Yksi", "kaksi", "Kolme")
    private var mTime: Calendar = Calendar.getInstance()
    private val mTimeFormatter = SimpleDateFormat("HH:mm", Locale("fi"))
    private val mDateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale("fi"))
    private lateinit var timeEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var departureRadioButton: RadioButton
    private val arrivalRadioButton: RadioButton? = view?.findViewById(R.id.routeOnArrivalRadioButton)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.routes_fragment, container, false)
        val routesOriginTextInput = view.findViewById<EditText>(R.id.routesOriginTextInput)

        this.timeEditText = view.findViewById(R.id.timeEditText)
        timeEditText.setText(mTimeFormatter.format(mTime.time))
        this.dateEditText = view.findViewById(R.id.dateEditText)
        dateEditText.setText(mDateFormatter.format(mTime.time))

        val routesOriginAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.routesOriginTextInput)
        ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, stops).also { adapter -> routesOriginAutoComplete.setAdapter(adapter)}

/*        dateEditText.setOnClickListener {
            val tpd = TabbedDialog()
            tpd.show(this.activity?.supportFragmentManager, "DateTimePickerDialog")
*//*            val fm = this.activity?.supportFragmentManager
            fm!!.beginTransaction()
                    .show(tpd)
                    .commit()*//*
            //tpd.show(this.activity?.fragmentManager, "TimePickerDialog")
            println("clicked")
        }*/

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

        val searchRoutesButton = view.findViewById<Button>(R.id.routesSearchRouteButton)
        searchRoutesButton.setOnClickListener { v -> startActivity(Intent(this.context, RouteResultsActivity::class.java)) }
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
}