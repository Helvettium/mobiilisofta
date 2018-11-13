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
    private double lng = 0.0;
    private double lat = 0.0;
    private String units = "metric";
    private static Context appContext;
    private String urlString = "http://api.openweathermap.org/data/2.5/forecast?";
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

    public void setCoordinates(double latitude, double longitude, String unit){
        if (latitude != 0.0){
            lat = latitude;
            lng = longitude;
            units = unit;

            urlString = "http://api.openweathermap.org/data/2.5/forecast?"
                    +"lat=" +latitude
                    +"&lon=" +longitude
                    +"&units="+unit
                    +"&appid=f826a6642b2ec59952540192647110bb";
        }
    }

    public void getAnyDataFromServer() {
        /*
         * Requesting weather information from API
         */
        RequestQueue queue = Volley.newRequestQueue(appContext);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
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
