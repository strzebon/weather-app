package pl.edu.agh.to2.example.models.weather.response;

import pl.edu.agh.to2.example.models.weather.Precipitation;
import pl.edu.agh.to2.example.models.weather.TemperatureLevel;

import java.util.List;

public record WeatherResponseConverted(
        List<String> locations,
        TemperatureLevel temperatureLevel,
        boolean isWindy,
        List<Precipitation> precipitation,
        double minTemp,
        double sensedTemp,
        double maxPrecip
) {
}
