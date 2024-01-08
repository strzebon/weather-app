package pl.edu.agh.to2.example.converters;

import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import static java.lang.Math.sqrt;

class WeatherCalculator {
    private static final String TEMP_EXCEPTION_MESSAGE = "Data about temperature not found";
    private static final String PRECIP_EXCEPTION_MESSAGE = "Data about precipitation not found";
    private static final String WIND_EXCEPTION_MESSAGE = "Data about wind not found";
    private static final int FLAG_TRUE = 1;

    static double findMinTemp(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble minTemp = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::temp_c)
                .min();
        if (minTemp.isEmpty()) {
            throw new MissingDataException(TEMP_EXCEPTION_MESSAGE);
        }
        return minTemp.getAsDouble();
    }

    static double findMaxPrecip(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxPrecip = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::precip_mm)
                .max();
        if (maxPrecip.isEmpty()) {
            throw new MissingDataException(PRECIP_EXCEPTION_MESSAGE);
        }
        return maxPrecip.getAsDouble();
    }

    static double findMaxWind(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxWind = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::wind_kph)
                .max();
        if (maxWind.isEmpty()) {
            throw new MissingDataException(WIND_EXCEPTION_MESSAGE);
        }
        return maxWind.getAsDouble();
    }

    static boolean checkWillItRain(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItRain = weatherPerHours.stream()
                .map(WeatherPerHour::will_it_rain)
                .filter(rain -> rain == FLAG_TRUE)
                .findAny();
        return willItRain.isPresent();
    }

    static boolean checkWillItSnow(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItSnow = weatherPerHours.stream()
                .map(WeatherPerHour::will_it_snow)
                .filter(snow -> snow == FLAG_TRUE)
                .findAny();
        return willItSnow.isPresent();
    }

    static double calculateSensedTemp(double temp, double wind) {
        return 33 + (0.478 + 0.237 * sqrt(wind) - 0.0124 * wind) * (temp - 33);
    }
}
