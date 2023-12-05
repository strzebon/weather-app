package pl.edu.agh.to2.example.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.to2.example.Main;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
class WeatherServiceTest {
    @Autowired
    private WeatherService service;

    @Test
    public void shouldReturnNullWhenWrongCoordinates() throws Exception {
        //when
        Optional<WeatherResponse> response = service.findWeather(new WeatherRequest(1000,1000));
        //then
        assertTrue(response.isEmpty());
    }
}
