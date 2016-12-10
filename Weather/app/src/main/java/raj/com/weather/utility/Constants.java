package raj.com.weather.utility;

public interface Constants {

    String appId = "a03ba89e935229a8e01e6ee197ad42e5";
    String cityId = "q=delhi,in"; //Delhi city by choice
    String currentTempUrl = "http://api.openweathermap.org/data/2.5/weather?"+ cityId + "&appid=" + appId +"&units=metric";
    String forcastUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?"+ cityId + "&appid=" + appId +"&units=metric&cnt=5";
    String iconUrl = "http://openweathermap.org/img/w/%s.png";
    String celciusTag = "\u00B0";
    String celciusFullTag = "\u2103";

    interface ACTION_BROADCAST_CONSTANTS {

        String ACTION_WEATHER_DATA_RECEIVED = "action.weather.data";
        String ACTION_WEATHER_DATA_RECEIVED_FAILED = "action.weather.data.failed";
    }

}
