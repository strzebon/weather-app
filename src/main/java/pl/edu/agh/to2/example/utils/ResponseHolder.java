package pl.edu.agh.to2.example.utils;

import pl.edu.agh.to2.example.models.weather.WeatherResponse;

public class ResponseHolder {
    private static WeatherResponse lastResponse = null;

    public static void updateLastResponse(WeatherResponse weatherResponse) {
        lastResponse = weatherResponse;
    }

    public static WeatherResponse getLastResponse() {
        return lastResponse;
    }

    public static boolean isLastResponse() {
        return lastResponse != null;
    }
}
