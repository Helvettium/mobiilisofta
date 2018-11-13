package asia.jokin.ohjelmistomobiili;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class FetchDataJavaSingleton {
    private static FetchDataJavaSingleton ourInstance = null;
    private static Context appContext;
    private String urlString = "http://api.publictransport.tampere.fi/prod/?user=zx123&pass=qmF:L}h3wR2n";
    static FetchDataJavaSingleton getInstance(Context context) {
        appContext = context;
        if( ourInstance == null ){
            ourInstance = new FetchDataJavaSingleton();
        }
        return ourInstance;
    }


    private FetchDataJavaSingleton() {}

    public List<StopData> getStopData() {
        return stopData;
    }
    public List<StopsData> getStopsData() {return stopsData; }
    public List<LineData> getLineData() {
        return lineData;
    }
    //public List<WeatherForecast> getWeatherForecast( int latitude, int longitude ){ return weatherForecast; }

    public void getStopsData(String locationLatitude, String locationLongitude){

}

    public void getLineData(String locationLatitude, String locationLongitude){

    }

    public void getStopData(String locationName){
        String searchTerm = "&code="+locationName;
        fetchData(searchTerm);
    }

    public void fetchData(String searchTerm) {
        /*
         * Requesting weather information from API
         */
        RequestQueue queue = Volley.newRequestQueue(appContext);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString+searchTerm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJsonAndConstructArrayList( response );
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText( appContext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void parseJsonAndConstructArrayList(String response) {
        /*
         * Parsing JSON and putting everything in WeatherForecast
         */
        stopData.clear();
        stopData.add(new StopData(response));
    }

    private ArrayList<StopData> stopData = new ArrayList<StopData>();
    private ArrayList<StopsData> stopsData = new ArrayList<StopsData>();
    private ArrayList<LineData> lineData = new ArrayList<LineData>();
}
