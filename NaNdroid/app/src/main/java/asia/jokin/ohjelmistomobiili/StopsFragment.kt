package asia.jokin.ohjelmistomobiili

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class StopsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.stops_fragment, container, false)
        //val textView = view.findViewById<TextView>(R.id.txtMain)
        //textView.setText("In stops")


        return view
    }
}