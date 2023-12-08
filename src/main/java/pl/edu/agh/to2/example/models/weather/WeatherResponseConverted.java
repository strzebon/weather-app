package pl.edu.agh.to2.example.models.weather;

import java.util.List;

public record WeatherResponseConverted(
        List<String> locations,
        Temperature temperature,
        boolean isWindy,
        Precipitation precipitation
) {
}
