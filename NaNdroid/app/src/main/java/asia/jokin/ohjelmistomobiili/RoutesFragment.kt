package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.Button


class RoutesFragment : Fragment() {
    private val stops = arrayOf("Yksi", "kaksi", "Kolme")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.routes_fragment, container, false)
        val routesOriginTextInput = view.findViewById<EditText>(R.id.routesOriginTextInput)

        val routesOriginAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.routesOriginTextInput)
        ArrayAdapter<String>(this.context, android.R.layout.simple_list_item_1, stops).also { adapter -> routesOriginAutoComplete.setAdapter(adapter)}


        val searchRoutesButton = view.findViewById<Button>(R.id.routesSearchRouteButton)
        searchRoutesButton.setOnClickListener { v -> startActivity(Intent(this.context, RouteResultsActivity::class.java)) }
        return view
    }
}