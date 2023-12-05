package pl.edu.agh.to2.example.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.example.models.dto.WeatherRequestDto;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;
import pl.edu.agh.to2.example.services.WeatherService;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin()
@RequestMapping("/weather")
public class WeatherController {
    WeatherService service;

    @Autowired
    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<WeatherResponse> findWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
        WeatherRequest weatherRequest = new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng());
        Optional<WeatherResponse> weatherResponse;
        try {
            weatherResponse = service.findWeather(weatherRequest);
        } catch (IOException ignored) {
            return new ResponseEntity<>(BAD_GATEWAY);
        }

        return weatherResponse
                .map(wr -> new ResponseEntity<>(wr, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherResponse> getLastWeatherResponse() {
        if (!service.isLastResponse()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(service.getLastResponse(), OK);
    }
}
