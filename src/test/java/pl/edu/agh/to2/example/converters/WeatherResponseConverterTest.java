package pl.edu.agh.to2.example.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

class WeatherResponseConverterTest {
    private List<WeatherForecastResponse> weatherForecastResponses;
    private static final String location1 = "London";
    private static final double VALUE_FOR_CONDITIONS = 10;

    @BeforeEach
    void setUp() {
        weatherForecastResponses = new ArrayList<>();
        weatherForecastResponses.add(new WeatherForecastResponse(location1, emptyList(), emptyList()));
    }

    @Test
    void shouldReturnProperResponseWhenGetProperData() {
        WeatherResponseConverted weatherResponseConverted = null;
        try (MockedStatic<WeatherCalculator> weatherCalculatorMocked = Mockito.mockStatic(WeatherCalculator.class)) {
            weatherCalculatorMocked.when(() -> WeatherCalculator.findMinTemp(any())).thenReturn(VALUE_FOR_CONDITIONS);
            weatherCalculatorMocked.when(() -> WeatherCalculator.findMaxPrecip(any())).thenReturn(VALUE_FOR_CONDITIONS);
            weatherCalculatorMocked.when(() -> WeatherCalculator.findMaxWind(any())).thenReturn(VALUE_FOR_CONDITIONS);
            weatherCalculatorMocked.when(() -> WeatherCalculator.checkWillItRain(any())).thenReturn(true);
            weatherCalculatorMocked.when(() -> WeatherCalculator.checkWillItSnow(any())).thenReturn(true);

            weatherResponseConverted = WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses);
        } catch (MissingDataException e) {
            e.printStackTrace();
        }
        assertNotNull(weatherResponseConverted);
        assertEquals(weatherResponseConverted.locations(), List.of(location1));
        assertEquals(weatherResponseConverted.minTemp(), VALUE_FOR_CONDITIONS);
        assertEquals(weatherResponseConverted.maxPrecip(), VALUE_FOR_CONDITIONS);
        assertTrue(weatherResponseConverted.isWindy());
    }


    @Test
    void shouldThrowExceptionWhenMethodInsideThrowsItAlso() {
        try (MockedStatic<WeatherCalculator> weatherCalculatorMocked = Mockito.mockStatic(WeatherCalculator.class)) {
            weatherCalculatorMocked.when(() -> WeatherCalculator.findMinTemp(any())).thenThrow(MissingDataException.class);

            assertThrows(MissingDataException.class,
                    () -> WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses));
        }
    }

}