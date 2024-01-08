package pl.edu.agh.to2.example.models.weather;

public record WeatherPerHour(
        String time,
        double temp_c,
        double precip_mm,
        double wind_kph,
        int will_it_rain,
        int will_it_snow
) {
}
