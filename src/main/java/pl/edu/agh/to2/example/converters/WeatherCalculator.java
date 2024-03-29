package pl.edu.agh.to2.example.converters;

import lombok.NoArgsConstructor;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherHistoryResponse;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import static java.lang.Math.sqrt;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class WeatherCalculator {
    private static final String TEMP_EXCEPTION_MESSAGE = "Data about temperature not found";
    private static final String PRECIP_EXCEPTION_MESSAGE = "Data about precipitation not found";
    private static final String WIND_EXCEPTION_MESSAGE = "Data about wind not found";
    private static final int FLAG_TRUE = 1;

    static double findMinTemp(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble minTemp = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::tempC)
                .min();
        if (minTemp.isEmpty()) {
            throw new MissingDataException(TEMP_EXCEPTION_MESSAGE);
        }
        return minTemp.getAsDouble();
    }

    static double findMaxPrecip(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxPrecip = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::precipMm)
                .max();
        if (maxPrecip.isEmpty()) {
            throw new MissingDataException(PRECIP_EXCEPTION_MESSAGE);
        }
        return maxPrecip.getAsDouble();
    }

    static double findMaxWind(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxWind = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::windKph)
                .max();
        if (maxWind.isEmpty()) {
            throw new MissingDataException(WIND_EXCEPTION_MESSAGE);
        }
        return maxWind.getAsDouble();
    }

    static boolean checkWillItRain(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItRain = weatherPerHours.stream()
                .map(WeatherPerHour::willItRain)
                .filter(rain -> rain == FLAG_TRUE)
                .findAny();
        return willItRain.isPresent();
    }

    static boolean checkWillItSnow(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItSnow = weatherPerHours.stream()
                .map(WeatherPerHour::willItSnow)
                .filter(snow -> snow == FLAG_TRUE)
                .findAny();
        return willItSnow.isPresent();
    }

    static double calculateSensedTemp(double temp, double wind) {
        return 33 + (0.478 + 0.237 * sqrt(wind) - 0.0124 * wind) * (temp - 33);
    }

    static boolean checkIsMuddy(
            WeatherHistoryResponse weatherHistoryResponse,
            List<WeatherForecastResponse> weatherForecastResponses,
            LocalDateTime currentTime,
            LocalDateTime latestTimeForToday
    ) {
        Optional<Integer> firstDay;
        Optional<Integer> secondDay;

        if (currentTime.isAfter(latestTimeForToday)) {
            firstDay = weatherHistoryResponse.wasRainySecondDay().stream()
                    .filter(e -> e == FLAG_TRUE)
                    .findAny();
            secondDay = weatherForecastResponses.stream()
                    .map(WeatherForecastResponse::todayWeather)
                    .flatMap(Collection::stream)
                    .toList()
                    .stream()
                    .map(WeatherPerHour::willItRain)
                    .filter(rain -> rain == FLAG_TRUE)
                    .findAny();
        } else {
            firstDay = weatherHistoryResponse.wasRainyFirstDay().stream()
                    .filter(e -> e == FLAG_TRUE)
                    .findAny();
            secondDay = weatherHistoryResponse.wasRainySecondDay().stream()
                    .filter(e -> e == FLAG_TRUE)
                    .findAny();
        }
        return (firstDay.isPresent() || secondDay.isPresent());
    }
}
