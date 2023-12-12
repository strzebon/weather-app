package pl.edu.agh.to2.example.utils;

//import pl.edu.agh.to2.example.models.weather.response.WeatherResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

public class ResponseHolder {
    private static WeatherResponseConverted lastResponse = null;

    public static void updateLastResponse(WeatherResponseConverted weatherResponse) {
        lastResponse = weatherResponse;
    }

    public static WeatherResponseConverted getLastResponse() {
        return lastResponse;
    }

    public static boolean isLastResponse() {
        return lastResponse != null;
    }
}
