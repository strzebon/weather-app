package pl.edu.agh.to2.example.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherHistoryResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherCalculatorTest {
    private List<WeatherPerHour> weatherPerHours;
    private WeatherHistoryResponse weatherHistoryResponse;
    private List<WeatherForecastResponse> weatherForecastResponses;
    private static final double TEMP_VAL = 10;
    private static final double PRECIP_VAL = 3;
    private static final double WIND_VAL = 6;
    private static final int FLAG_FALSE = 0;
    private static final int FLAG_TRUE = 1;

    @BeforeEach
    void setUp() {
        weatherPerHours = new ArrayList<>();
        weatherPerHours.add(new WeatherPerHour(null, TEMP_VAL, PRECIP_VAL, WIND_VAL, FLAG_FALSE, FLAG_FALSE));
        weatherHistoryResponse = new WeatherHistoryResponse(new ArrayList<>(), new ArrayList<>());
        weatherHistoryResponse.wasRainyFirstDay().add(0);
        weatherHistoryResponse.wasRainySecondDay().add(0);
        weatherForecastResponses = new ArrayList<>();
        WeatherForecastResponse response = new WeatherForecastResponse(null, weatherPerHours, weatherPerHours);
        weatherForecastResponses.add(response);
    }

    @Test
    void shouldFindMinTempProperly() throws MissingDataException {
        // given
        double lowestTemp = -1.0;
        WeatherPerHour nextWeatherPerHour = new WeatherPerHour(null, lowestTemp, PRECIP_VAL, WIND_VAL, FLAG_FALSE, FLAG_FALSE);
        weatherPerHours.add(nextWeatherPerHour);

        //when
        double minTemp = WeatherCalculator.findMinTemp(weatherPerHours);

        //then
        assertEquals(lowestTemp, minTemp);
    }

    @Test
    void shouldThrowExceptionWhenEmptyListWithTemps() {
        // given
        weatherPerHours = emptyList();

        assertThrows(MissingDataException.class, () -> WeatherCalculator.findMinTemp(weatherPerHours));
    }

    @Test
    void shouldFindMaxPrecipitationProperly() throws MissingDataException {
        // given
        double highestPrecip = 5.0;
        WeatherPerHour nextWeatherPerHour = new WeatherPerHour(null, TEMP_VAL, highestPrecip, WIND_VAL, FLAG_FALSE, FLAG_FALSE);
        weatherPerHours.add(nextWeatherPerHour);

        // when
        double maxPrecip = WeatherCalculator.findMaxPrecip(weatherPerHours);

        // then
        assertEquals(highestPrecip, maxPrecip);
    }

    @Test
    void shouldThrowExceptionWhenEmptyListWithPrecipitation() {
        // given
        weatherPerHours = emptyList();

        assertThrows(MissingDataException.class, () -> WeatherCalculator.findMaxPrecip(weatherPerHours));
    }

    @Test
    void shouldFindMaxWindProperly() throws MissingDataException {
        // given
        double highestWind = 7.0;
        WeatherPerHour nextWeatherPerHour = new WeatherPerHour(null, TEMP_VAL, PRECIP_VAL, highestWind, FLAG_FALSE, FLAG_FALSE);
        weatherPerHours.add(nextWeatherPerHour);

        // when
        double maxWind = WeatherCalculator.findMaxWind(weatherPerHours);

        // then
        assertEquals(highestWind, maxWind);
    }

    @Test
    void shouldThrowExceptionWhenEmptyListWithWind() {
        // given
        weatherPerHours = emptyList();

        assertThrows(MissingDataException.class, () -> WeatherCalculator.findMaxWind(weatherPerHours));
    }

    @Test
    void checkWillItRainShouldReturnTrue() {
        // given
        int flagTrue = 1;
        WeatherPerHour nextWeatherPerHour = new WeatherPerHour(null, TEMP_VAL, PRECIP_VAL, WIND_VAL, flagTrue, FLAG_FALSE);
        weatherPerHours.add(nextWeatherPerHour);

        // then
        assertTrue(WeatherCalculator.checkWillItRain(weatherPerHours));
    }

    @Test
    void checkWillItRainShouldReturnFalse() {
        // then
        assertFalse(WeatherCalculator.checkWillItRain(weatherPerHours));
    }

    @Test
    void checkWillItSnowShouldReturnTrue() {
        // given
        int flagTrue = 1;
        WeatherPerHour nextWeatherPerHour = new WeatherPerHour(null, TEMP_VAL, PRECIP_VAL, WIND_VAL, FLAG_FALSE, flagTrue);
        weatherPerHours.add(nextWeatherPerHour);

        // then
        assertTrue(WeatherCalculator.checkWillItSnow(weatherPerHours));
    }

    @Test
    void checkWillItSnowShouldReturnFalse() {
        // then
        assertFalse(WeatherCalculator.checkWillItSnow(weatherPerHours));
    }

    @Test
    void shouldCalculateSensedTempProperly() {
        // given
        double temp = 9.0;
        double wind = 1.0;
        double expectedSensedTemp = 16.1376;

        // when
        double actualSensedTemp = WeatherCalculator.calculateSensedTemp(temp, wind);

        // then
        assertEquals(expectedSensedTemp, actualSensedTemp);
    }

    @Test
    void isMuddyShouldReturnTrueBeforeSix() {
        // given
        LocalDateTime date = mock(LocalDateTime.class);
        LocalDateTime lastDate = mock(LocalDateTime.class);
        weatherHistoryResponse.wasRainyFirstDay().add(1);
        when(date.isAfter(lastDate)).thenReturn(false);

        // when
        boolean isMuddy = WeatherCalculator.checkIsMuddy(weatherHistoryResponse, weatherForecastResponses, date, lastDate);

        // then
        assertTrue(isMuddy);
    }

    @Test
    void isMuddyShouldReturnFalseBeforeSix() {
        // given
        LocalDateTime date = mock(LocalDateTime.class);
        LocalDateTime lastDate = mock(LocalDateTime.class);
        when(date.isAfter(lastDate)).thenReturn(false);
        List<WeatherForecastResponse> weatherForecastResponses = mock(List.class);

        // when
        boolean isMuddy = WeatherCalculator.checkIsMuddy(weatherHistoryResponse, weatherForecastResponses, date, lastDate);

        // then
        assertFalse(isMuddy);
    }

    @Test
    void isMuddyShouldReturnTrueAfterSix() {
        // given
        LocalDateTime date = mock(LocalDateTime.class);
        LocalDateTime lastDate = mock(LocalDateTime.class);
        weatherHistoryResponse.wasRainyFirstDay().add(1);
        when(date.isAfter(lastDate)).thenReturn(true);
        List<WeatherPerHour> weather = List.of(new WeatherPerHour(null, TEMP_VAL, PRECIP_VAL, WIND_VAL, FLAG_TRUE, FLAG_FALSE));
        WeatherForecastResponse response = new WeatherForecastResponse(null, weather, weatherPerHours);
        weatherForecastResponses.add(response);

        // when
        boolean isMuddy = WeatherCalculator.checkIsMuddy(weatherHistoryResponse, weatherForecastResponses, date, lastDate);

        // then
        assertTrue(isMuddy);
    }

    @Test
    void isMuddyShouldReturnFalseAfterSix() {
        // given
        LocalDateTime date = mock(LocalDateTime.class);
        LocalDateTime lastDate = mock(LocalDateTime.class);
        weatherHistoryResponse.wasRainyFirstDay().add(1);
        when(date.isAfter(lastDate)).thenReturn(true);

        // when
        boolean isMuddy = WeatherCalculator.checkIsMuddy(weatherHistoryResponse, weatherForecastResponses, date, lastDate);

        // then
        assertFalse(isMuddy);
    }
}