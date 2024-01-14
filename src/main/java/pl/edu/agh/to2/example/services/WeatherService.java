package pl.edu.agh.to2.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.example.converters.WeatherResponseConverter;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherHistoryResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;
import pl.edu.agh.to2.example.utils.ResponseHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherService {
    private final ForecastService forecastService;
    private final HistoryService historyService;

    @Autowired
    public WeatherService(ForecastService forecastService, HistoryService historyService) {
        this.forecastService = forecastService;
        this.historyService = historyService;
    }

    public Optional<WeatherResponseConverted> findWeather(List<WeatherRequest> weatherRequests) throws IOException {
        List<WeatherForecastResponse> weatherForecastResponses = forecastService.findWeatherForecast(weatherRequests);
        WeatherHistoryResponse weatherHistoryResponse = historyService.findWeatherHistory(weatherRequests);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime latestTimeForToday = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
                currentTime.getDayOfMonth(), 6, 0);
        try {
            ResponseHolder.updateLastResponse(WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses, weatherHistoryResponse, currentTime, latestTimeForToday));
            return Optional.of(WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses, weatherHistoryResponse, currentTime, latestTimeForToday));
        } catch (MissingDataException e) {
            return Optional.empty();
        }
    }
}
