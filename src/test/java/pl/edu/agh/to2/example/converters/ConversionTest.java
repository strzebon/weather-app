package pl.edu.agh.to2.example.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.Precipitation;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.CHILLY;

class ConversionTest {
    private List<WeatherForecastResponse> weatherForecastResponses;
    private static final String location1 = "London";
    private static final double MIN_TEMP = 5.0;
    private static final double MAX_WIND = 3.0;
    private static final double MAX_PRECIP = 10.0;

    @BeforeEach
    void setUp() {
        weatherForecastResponses = new ArrayList<>();
        List<WeatherPerHour> weatherPerHours = new ArrayList<>();
        weatherPerHours.add(new WeatherPerHour(null, 11, MAX_PRECIP, MAX_WIND, 0, 0));
        weatherPerHours.add(new WeatherPerHour(null, 15, 3, 2, 0, 0));
        weatherPerHours.add(new WeatherPerHour(null, MIN_TEMP, 5, 1, 0, 0));
        weatherForecastResponses.add(new WeatherForecastResponse(location1, weatherPerHours, weatherPerHours));
    }

    @Test
    void convertWeatherResponseGiveProperResponse() throws MissingDataException {
        //given
        double expectedSenseTemp = 33 + (0.478 + 0.237 * sqrt(MAX_WIND) - 0.0124 * MAX_WIND) * (MIN_TEMP - 33);

        //when
        WeatherResponseConverted weatherResponseConverted = WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses);

        //then
        assertEquals(List.of(location1), weatherResponseConverted.locations());
        assertEquals(CHILLY, weatherResponseConverted.temperatureLevel());
        assertFalse(weatherResponseConverted.isWindy());
        assertEquals(List.of(Precipitation.CLEAR), weatherResponseConverted.precipitation());
        assertEquals(MIN_TEMP, weatherResponseConverted.minTemp());
        assertEquals(MAX_PRECIP, weatherResponseConverted.maxPrecip());
        assertEquals(expectedSenseTemp, weatherResponseConverted.sensedTemp());
    }
}
