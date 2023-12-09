package pl.edu.agh.to2.example.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.dto.WeatherRequestDto;
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;
import pl.edu.agh.to2.example.services.WeatherService;
import pl.edu.agh.to2.example.utils.ResponseHolder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin()
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService service;

    @Autowired
    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<WeatherResponseConverted> findWeather(@RequestBody List<WeatherRequestDto> weatherRequestsDto) {
        List<WeatherRequest> weatherRequests = weatherRequestsDto.stream()
                .map(weatherRequestDto -> new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng()))
                .toList();
        Optional<WeatherResponseConverted> weatherResponse;
        try {
            weatherResponse = service.findWeatherForecast(weatherRequests);
        } catch (IOException | MissingDataException ignored) {
            return new ResponseEntity<>(BAD_GATEWAY);
        }

        return weatherResponse
                .map(wr -> new ResponseEntity<>(wr, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherResponse> getLastWeatherResponse() {
        if (!ResponseHolder.isLastResponse()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(ResponseHolder.getLastResponse(), OK);
    }
}
