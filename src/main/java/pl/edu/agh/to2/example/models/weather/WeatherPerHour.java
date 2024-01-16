package pl.edu.agh.to2.example.models.weather;

import com.google.gson.annotations.SerializedName;

public record WeatherPerHour(
        String time,
        @SerializedName("temp_c")
        double tempC,
        @SerializedName("precip_mm")
        double precipMm,
        @SerializedName("wind_kph")
        double windKph,
        @SerializedName("will_it_rain")
        int willItRain,
        @SerializedName("will_it_snow")
        int willItSnow
) {
}
