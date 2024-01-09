package pl.edu.agh.to2.example.models.weather.response;

import java.util.List;

public record WeatherHistoryResponse(
    List<Integer> wasRainyFirstDay,
    List<Integer> wasRainySecondDay
) {
}
