package pl.edu.agh.to2.example.models.weather;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.COLD;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.FREEZING;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.HOT;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.WARM;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.determineTemperatureLevel;

class TemperatureLevelTest {

    @Test
    void shouldReturnFreezingTemperatureLevelWhenUnderTheirLevel() {
        // given
        double freezingTemperature = -15.0;
        TemperatureLevel expectedLevel = FREEZING;

        // when
        TemperatureLevel actualLevel = determineTemperatureLevel(freezingTemperature);

        // then
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void shouldReturnColdTemperatureLevelWhenUnderTheirLevel() {
        // given
        double freezingTemperature = -3.0;
        TemperatureLevel expectedLevel = COLD;

        // when
        TemperatureLevel actualLevel = determineTemperatureLevel(freezingTemperature);

        // then
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void shouldReturnWarmTemperatureLevelWhenUnderTheirLevel() {
        // given
        double freezingTemperature = 12.0;
        TemperatureLevel expectedLevel = WARM;

        // when
        TemperatureLevel actualLevel = determineTemperatureLevel(freezingTemperature);

        // then
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void shouldReturnHotTemperatureLevelWhenUnderTheirLevel() {
        // given
        double freezingTemperature = 25.0;
        TemperatureLevel expectedLevel = HOT;

        // when
        TemperatureLevel actualLevel = determineTemperatureLevel(freezingTemperature);

        // then
        assertEquals(expectedLevel, actualLevel);
    }

}