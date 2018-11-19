package asia.jokin.ohjelmistomobiili

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class FavouritesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view = inflater.inflate(R.layout.favourites_fragment, container, false)
        //val textView = view.findViewById<TextView>(R.id.txtMain)
        //textView.setText("In Favourites")

        // TODO favourites sisalto

        val btnPopup = view.findViewById<Button>(R.id.btnPopup)

        btnPopup.setOnClickListener(){
            val popupIntent = Intent(activity, PopupActivity::class.java)
            popupIntent.putExtra("stopcode", "0035")
            startActivity(popupIntent)
        }

        return view
    }
}