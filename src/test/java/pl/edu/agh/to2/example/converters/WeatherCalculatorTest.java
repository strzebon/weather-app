package pl.edu.agh.to2.example.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherCalculatorTest {
    private List<WeatherPerHour> weatherPerHours;
    private static final double TEMP_VAL = 10;
    private static final double PRECIP_VAL = 3;
    private static final double WIND_VAL = 6;
    private static final int FLAG_TRUE = 1;

    @BeforeEach
    void setUp() {
        weatherPerHours = new ArrayList<>();
        weatherPerHours.add(new WeatherPerHour(null, TEMP_VAL, PRECIP_VAL, WIND_VAL, FLAG_TRUE, FLAG_TRUE));
    }

    @Test
    void shouldFindMinTempProperly() throws MissingDataException {
        // given
        double lowestTemp = -1.0;
        WeatherPerHour nextWeatherPerHour = new WeatherPerHour(null, lowestTemp, PRECIP_VAL, WIND_VAL, FLAG_TRUE, FLAG_TRUE);
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
    void findMaxPrecip() {
    }

    @Test
    void findMaxWind() {
    }

    @Test
    void checkWillItRain() {
    }

    @Test
    void checkWillItSnow() {
    }

    @Test
    void calculateSensedTemp() {
    }
}