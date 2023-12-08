package pl.edu.agh.to2.example.models.weather;

import java.util.List;

public record WeatherMultiRequest(
        List<WeatherRequest> weatherRequests
) {
}
