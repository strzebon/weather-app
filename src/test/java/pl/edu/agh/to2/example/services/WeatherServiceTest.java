package pl.edu.agh.to2.example.services;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.to2.example.Main;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
public class WeatherServiceTest {
    WeatherService service = new WeatherService();

    @Test
    public void shouldReturnNullWhenNull() throws Exception {
        //when
        WeatherResponse response = service.findWeather(new WeatherRequest(1000,1000));
        //then
        assertEquals(response, null);
    }
}
