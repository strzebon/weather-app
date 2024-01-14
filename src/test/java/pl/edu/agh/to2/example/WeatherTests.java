package pl.edu.agh.to2.example;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;
import pl.edu.agh.to2.example.services.ForecastService;
import pl.edu.agh.to2.example.services.WeatherService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
class WeatherTests {
    @Autowired
    private WeatherService service;

    @Test
    void shouldReturnEmptyWhenWrongCoordinates() {
        try {
            //when
            Optional<WeatherResponseConverted> response = service.findWeather(List.of(new WeatherRequest(1000, 1000)));
            //then
            assertTrue(response.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void shouldReturnNonemptyWhenRightCoordinates() {
        try {
            //when
            Optional<WeatherResponseConverted> response = service.findWeather(List.of(new WeatherRequest(1, 1)));
            //then
            assertTrue(response.isPresent());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
