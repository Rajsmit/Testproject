package raj.com.weather.service;

import com.google.gson.Gson;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import raj.com.weather.model.Daily;
import raj.com.weather.model.Forcast;
import raj.com.weather.utility.Constants;

/**
 * Singleton class which loads the Weather report.
 */
public class WeatherService {

    private static Context mContext;
    private final RequestQueue mRequestQueue;
    private Forcast mWeatherForecast = null;

    public Daily getCurrent() {
        return mCurrent;
    }

    private Daily mCurrent = null;

    public Forcast getWeatherForecast() {
        return mWeatherForecast;
    }


    private static WeatherService mWeatherService;

    private WeatherService(Context context){
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public static synchronized WeatherService getInstance(Context context) {
        if (mWeatherService == null) {
            mWeatherService = new WeatherService(context);
        }
        return mWeatherService;
    }


    public void loadWeatherData( String cityName) {
        String requestUrl = Constants.currentTempUrl;
        requestUrl = requestUrl.replace("%s", cityName);
        JsonObjectRequest reqCurrent = new JsonObjectRequest(requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            mCurrent = new Gson().fromJson(response.toString(), Daily.class);
                            System.out.println(mCurrent.toString());
                            Intent intent = new Intent();
                            intent.setAction(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED_FAILED);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });
        String requestforcastUrl = Constants.forcastUrl;
        requestforcastUrl = requestforcastUrl.replace("%s", cityName);

        JsonObjectRequest reqForcast = new JsonObjectRequest(requestforcastUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            mWeatherForecast = new Gson().fromJson(response.toString(), Forcast.class);
                            Intent intent = new Intent();
                            intent.setAction(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED_FAILED);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });
        mRequestQueue.add(reqCurrent);
        mRequestQueue.add(reqForcast);
    }

}
