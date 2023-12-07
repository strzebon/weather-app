package pl.edu.agh.to2.example.models.weather;

import java.util.List;

public record WeatherForecastResponse(
        String location,
        List<WeatherPerHour> todayWeather,
        List<WeatherPerHour> tomorrowWeather
) {
}
