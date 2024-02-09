package pl.edu.agh.to2.example.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.Precipitation;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherHistoryResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.CHILLY;

class ConversionTest {
    private List<WeatherForecastResponse> weatherForecastResponses;
    private WeatherHistoryResponse weatherHistoryResponse;
    private static final String location1 = "London";
    private static final double MIN_TEMP_TODAY = 5.0;
    private static final double MAX_WIND_TODAY = 3.0;
    private static final double MAX_PRECIP_TODAY = 10.0;
    private static final double MIN_TEMP_TOMORROW = 4.0;
    private static final double MAX_WIND_TOMORROW = 4.0;
    private static final double MAX_PRECIP_TOMORROW = 11.0;

    @BeforeEach
    void setUp() {
        weatherForecastResponses = new ArrayList<>();
        List<WeatherPerHour> todayWeather = new ArrayList<>();
        todayWeather.add(new WeatherPerHour(null, 11, MAX_PRECIP_TODAY, MAX_WIND_TODAY, 0, 0));
        todayWeather.add(new WeatherPerHour(null, 15, 3, 2, 0, 0));
        todayWeather.add(new WeatherPerHour(null, MIN_TEMP_TODAY, 5, 1, 0, 0));
        List<WeatherPerHour> tomorrowWeather = new ArrayList<>();
        tomorrowWeather.add(new WeatherPerHour(null, 11, MAX_PRECIP_TOMORROW, MAX_WIND_TOMORROW, 0, 0));
        tomorrowWeather.add(new WeatherPerHour(null, 15, 3, 2, 0, 0));
        tomorrowWeather.add(new WeatherPerHour(null, MIN_TEMP_TOMORROW, 5, 1, 0, 0));
        weatherForecastResponses.add(new WeatherForecastResponse(location1, todayWeather, tomorrowWeather));
        weatherHistoryResponse = new WeatherHistoryResponse(List.of(0, 0, 0), List.of(0, 0, 0));
    }

    @Test
    void convertWeatherResponseGiveProperResponseBeforeSix() throws MissingDataException {
        //given
        double expectedSenseTemp = 33 + (0.478 + 0.237 * sqrt(MAX_WIND_TODAY) - 0.0124 * MAX_WIND_TODAY) * (MIN_TEMP_TODAY - 33);
        LocalDateTime currentTime = mock(LocalDateTime.class);
        LocalDateTime latestTimeForToday = mock(LocalDateTime.class);
        when(currentTime.isAfter(latestTimeForToday)).thenReturn(false);

        //when
        WeatherResponseConverted weatherResponseConverted = WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses, weatherHistoryResponse, currentTime, latestTimeForToday);

        //then
        assertEquals(List.of(location1), weatherResponseConverted.locations());
        assertEquals(CHILLY, weatherResponseConverted.temperatureLevel());
        assertFalse(weatherResponseConverted.isWindy());
        assertEquals(List.of(Precipitation.CLEAR), weatherResponseConverted.precipitation());
        assertEquals(MIN_TEMP_TODAY, weatherResponseConverted.minTemp());
        assertEquals(MAX_PRECIP_TODAY, weatherResponseConverted.maxPrecip());
        assertEquals(expectedSenseTemp, weatherResponseConverted.sensedTemp());
        assertFalse(weatherResponseConverted.isMuddy());
    }

    @Test
    void convertWeatherResponseGiveProperResponseAfterSix() throws MissingDataException {
        //given
        double expectedSenseTemp = 33 + (0.478 + 0.237 * sqrt(MAX_WIND_TOMORROW) - 0.0124 * MAX_WIND_TOMORROW) * (MIN_TEMP_TOMORROW - 33);
        LocalDateTime currentTime = mock(LocalDateTime.class);
        LocalDateTime latestTimeForToday = mock(LocalDateTime.class);
        when(currentTime.isAfter(latestTimeForToday)).thenReturn(true);

        //when
        WeatherResponseConverted weatherResponseConverted = WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses, weatherHistoryResponse, currentTime, latestTimeForToday);

        //then
        assertEquals(List.of(location1), weatherResponseConverted.locations());
        assertEquals(CHILLY, weatherResponseConverted.temperatureLevel());
        assertFalse(weatherResponseConverted.isWindy());
        assertEquals(List.of(Precipitation.CLEAR), weatherResponseConverted.precipitation());
        assertEquals(MIN_TEMP_TOMORROW, weatherResponseConverted.minTemp());
        assertEquals(MAX_PRECIP_TOMORROW, weatherResponseConverted.maxPrecip());
        assertEquals(expectedSenseTemp, weatherResponseConverted.sensedTemp());
        assertFalse(weatherResponseConverted.isMuddy());
    }
}
