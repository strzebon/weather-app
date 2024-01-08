package pl.edu.agh.to2.example.models.weather;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.determineTemperatureLevel;

class TemperatureLevelTest {
    @ParameterizedTest
    @CsvSource({"FREEZING, -15.0", "COLD, -2.0", "CHILLY, 5", "WARM, 12.0", "HOT, 23"})
    void shouldReturnProperTemperatureLevelWhenUnderTheirLevel(TemperatureLevel expectedLevel, double temperature) {
        // when
        TemperatureLevel actualLevel = determineTemperatureLevel(temperature);

        // then
        assertEquals(expectedLevel, actualLevel);
    }
}