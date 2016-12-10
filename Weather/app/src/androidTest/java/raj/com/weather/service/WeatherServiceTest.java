package raj.com.weather.service;

import org.junit.Test;

import android.test.AndroidTestCase;

import raj.com.weather.model.Daily;
import raj.com.weather.model.Forcast;


public class WeatherServiceTest extends AndroidTestCase{
    @Test
    public void getCurrent() throws Exception {
        Daily ins = WeatherService.getInstance(mContext).getCurrent();
        assertTrue(null == ins);
    }

    @Test
    public void getWeatherForecast() throws Exception {
        Forcast fCast = WeatherService.getInstance(mContext).getWeatherForecast();
        assertTrue(null == fCast);
    }

    @Test
    public void getInstance() throws Exception {
        WeatherService ins = WeatherService.getInstance(mContext);
        assertTrue(null != ins);
    }

}