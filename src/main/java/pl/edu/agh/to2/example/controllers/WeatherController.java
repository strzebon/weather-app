package pl.edu.agh.to2.example.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.example.models.dto.WeatherRequestDto;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;
import pl.edu.agh.to2.example.services.WeatherService;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    WeatherService service;

    @Autowired
    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<WeatherResponse> findWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
        WeatherRequest weatherRequest = new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng());
        WeatherResponse weatherResponse = service.findWeather(weatherRequest);
        if (weatherResponse == null) {
            return new ResponseEntity<>(null, NOT_FOUND);
        }
        return new ResponseEntity<>(weatherResponse, OK);
    }
}
