package asia.jokin.ohjelmistomobiili

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class PopupFragment : Fragment() {
    private lateinit var mTitleText: TextView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mViewAdapter: RecyclerView.Adapter<*>
    private lateinit var mViewManager: RecyclerView.LayoutManager

    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        return aInflater.inflate(R.layout.popup_fragment, aContainer, false)
    }

    override fun onViewCreated(aView: View, aSavedInstanceState: Bundle?) {
        super.onViewCreated(aView, aSavedInstanceState)

        mTitleText = aView.findViewById(R.id.popupTitle)
    }

    fun showStop(aStopCode: Int) {

        // Debug
        Log.d("Marker", aStopCode.toString())

        //Toast.makeText(this, "click", Toast.LENGTH_LONG).show()

        // Vitun raivostuttava tapa viitata ID
        // mTitleText.text = aStopCode
    }
}