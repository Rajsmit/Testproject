package raj.com.weather;

import com.android.volley.toolbox.NetworkImageView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import raj.com.weather.model.Daily;
import raj.com.weather.model.Forcast;
import raj.com.weather.service.ImageLoaderService;
import raj.com.weather.service.WeatherService;
import raj.com.weather.utility.Constants;

/**
 * A weather form screen that shows weather of selected city.
 */
public class WeatherActivity extends AppCompatActivity {

    // UI references.
    private View mProgressView;
    private View mWeatherFormView;
    private View mErrorView;
    private View mDay1View;
    private View mDay2View;
    private View mDay3View;
    private View mDay4View;
    private View mDay5View;
    private Daily mCurrent;
    private TextView mCity;
    private TextView mCurrentDay;
    private TextView mWeatherText;
    private NetworkImageView mCurentImage;
    private TextView mCurrentTemp;
    private TextView mWind;
    private TextView mHumidity;

    //weather data holder
    private Forcast mWeatherForecast;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equalsIgnoreCase(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED)) {
                    initViews();
                    showProgress(false, false);
                } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED_FAILED)) {
                    Toast.makeText(WeatherActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    showProgress(true, false);
                }
            }
        }

    };

    /**
     * This API will initializa the views of main screen
     */
    private void initViews() {
        mWeatherForecast = WeatherService.getInstance(this).getWeatherForecast();
        mCurrent = WeatherService.getInstance(this).getCurrent();

        if (null != mCurrent) {
            initMainlayout();
        }
        if (null != mWeatherForecast) {
            initForcastlayout();
        }
    }

    /**
     * Initialize the 5 days forcast layout
     */
    private void initForcastlayout() {
        initDayslayout(mDay1View, 0);
        initDayslayout(mDay2View, 1);
        initDayslayout(mDay3View, 2);
        initDayslayout(mDay4View, 3);
        initDayslayout(mDay5View, 4);
    }

    private void initMainlayout() {
        mCity.setText(mCurrent.getName());
        mWeatherText.setText(mCurrent.getWeather().get(0).getMain());
        mCurrentTemp.setText(mCurrent.getMain().getTemp().toString() + Constants.celciusFullTag);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        mCurrentDay.setText(dayOfTheWeek);
        mCurentImage.setImageUrl( Constants.iconUrl.replace("%s", mCurrent.getWeather().get(0).getIcon()), ImageLoaderService.getInstance(this).getImageLoader());
        mCurentImage.setDefaultImageResId(R.drawable.placeholder);
        mHumidity.setText(getResources().getString(R.string.humidity) + mCurrent.getMain().getHumidity().toString()+ "%");
        mWind.setText(getResources().getString(R.string.wind) + mCurrent.getWind().getSpeed().toString()+ "m/s");
    }

    private void initDayslayout(View view, int count) {

        TextView dayView = (TextView)view.findViewById(R.id.dayName);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(mWeatherForecast.getList().get(count).getDt() * 1000L);
        cal.setTimeZone(TimeZone.getDefault());
        String day = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        dayView.setText(day);
        NetworkImageView icon = (NetworkImageView)view.findViewById(R.id.imageviewicon);
        icon.setImageUrl( Constants.iconUrl.replace("%s", mWeatherForecast.getList().get(count).getWeather().get(0).getIcon()), ImageLoaderService.getInstance(this).getImageLoader());
        icon.setDefaultImageResId(R.drawable.placeholder);
        TextView highTemp = (TextView)view.findViewById(R.id.highTemp);
        TextView lowTemp = (TextView)view.findViewById(R.id.lowTemp);
        highTemp.setText(mWeatherForecast.getList().get(count).getTemp().getDay().toString()+ Constants.celciusTag);
        lowTemp.setText(mWeatherForecast.getList().get(count).getTemp().getMin().toString()+ Constants.celciusTag);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mWeatherFormView = findViewById(R.id.weather_form);
        mProgressView = findViewById(R.id.weather_progress);
        mErrorView = findViewById(R.id.loading_error);

        mCity = (TextView)findViewById(R.id.city);
        mCurrentDay = (TextView)findViewById(R.id.days);
        mWeatherText = (TextView)findViewById(R.id.weathertext);
        mCurrentTemp = (TextView)findViewById(R.id.currentTemp);
        mCurentImage = (NetworkImageView)findViewById(R.id.imageView);
        mDay1View = findViewById(R.id.day1);
        mDay2View = findViewById(R.id.day2);
        mDay3View = findViewById(R.id.day3);
        mDay4View = findViewById(R.id.day4);
        mDay5View = findViewById(R.id.day5);
        mWind = (TextView)findViewById(R.id.wind);
        mHumidity = (TextView)findViewById(R.id.humidity);
        Button refreshView = (Button) findViewById(R.id.refresh);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(false, true);
                WeatherService.getInstance(WeatherActivity.this).loadWeatherData();
            }
        });
        showProgress(false, true);
        WeatherService.getInstance(this).loadWeatherData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED);
        filter.addAction(Constants.ACTION_BROADCAST_CONSTANTS.ACTION_WEATHER_DATA_RECEIVED_FAILED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Shows the progress UI and hides the weather form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean bErrorView, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            if(bErrorView){
                mWeatherFormView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
            }else {
                mWeatherFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
            mWeatherFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(bErrorView){
                        mWeatherFormView.setVisibility(View.GONE);
                        mErrorView.setVisibility(View.VISIBLE);
                    }else {
                        mWeatherFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }

                }
            });
            if(bErrorView){
                mErrorView.setVisibility(View.VISIBLE);
            }
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            if(!bErrorView) {
                mWeatherFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }

}

