package pl.edu.agh.to2.example.models.weather;

public record WeatherResponse(
        String location,
        double temp_c,
        String img_path,
        String condition,
        double precip_mm
) {
}