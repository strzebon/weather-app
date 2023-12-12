package pl.edu.agh.to2.example.models.weather.request;

import java.util.List;

public record WeatherMultiRequest(
        List<WeatherRequest> weatherRequests
) {
}
