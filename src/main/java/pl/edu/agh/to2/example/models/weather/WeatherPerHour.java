package pl.edu.agh.to2.example.models.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherPerHour(
        String time,
        @JsonProperty("temp_c")
        double tempC,
        @JsonProperty("precip_mm")
        double precipMm,
        @JsonProperty("wind_kph")
        double windKph,
        @JsonProperty("will_it_rain")
        int willItRain,
        @JsonProperty("will_it_snow")
        int willItSnow
) {
}
