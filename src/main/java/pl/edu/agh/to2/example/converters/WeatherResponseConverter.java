package pl.edu.agh.to2.example.converters;

import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.Precipitation;
import pl.edu.agh.to2.example.models.weather.TemperatureLevel;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pl.edu.agh.to2.example.converters.WeatherCalculator.calculateSensedTemp;
import static pl.edu.agh.to2.example.converters.WeatherCalculator.checkWillItRain;
import static pl.edu.agh.to2.example.converters.WeatherCalculator.checkWillItSnow;
import static pl.edu.agh.to2.example.converters.WeatherCalculator.findMaxPrecip;
import static pl.edu.agh.to2.example.converters.WeatherCalculator.findMaxWind;
import static pl.edu.agh.to2.example.converters.WeatherCalculator.findMinTemp;
import static pl.edu.agh.to2.example.models.weather.Precipitation.*;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.determineTemperatureLevel;

public class WeatherResponseConverter {
    private static final double MIN_WINDY_VALUE = 5;

    public static WeatherResponseConverted convertWeatherResponse(List<WeatherForecastResponse> weatherForecastResponses)
            throws MissingDataException {
        List<WeatherPerHour> weatherPerHours = prepareDataToAnalysis(weatherForecastResponses);
        List<String> locations = getLocations(weatherForecastResponses);

        double minTemp = findMinTemp(weatherPerHours);
        double maxPrecip = findMaxPrecip(weatherPerHours);
        double maxWind = findMaxWind(weatherPerHours);
        boolean willItRain = checkWillItRain(weatherPerHours);
        boolean willItSnow = checkWillItSnow(weatherPerHours);
        double sensedTemp = calculateSensedTemp(minTemp, maxWind);

        TemperatureLevel temperatureLevel = determineTemperatureLevel(sensedTemp);
        boolean isWindy = determineIsWindy(maxWind);
        List<Precipitation> precipitations = prepareListOfPrecipitations(willItRain, willItSnow);

        return new WeatherResponseConverted(locations, temperatureLevel, isWindy, precipitations, minTemp, sensedTemp, maxPrecip);
    }

    private static List<String> getLocations(List<WeatherForecastResponse> weatherForecastResponses) {
        return weatherForecastResponses.stream()
                .map(WeatherForecastResponse::location)
                .toList();
    }

    private static List<WeatherPerHour> prepareDataToAnalysis(List<WeatherForecastResponse> weatherForecastResponses) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime latestTimeForToday = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
                currentTime.getDayOfMonth(), 6, 0);
        if (currentTime.isAfter(latestTimeForToday)) {
            return weatherForecastResponses.stream()
                    .map(WeatherForecastResponse::tomorrowWeather)
                    .flatMap(Collection::stream)
                    .toList();
        }
        return weatherForecastResponses.stream()
                .map(WeatherForecastResponse::todayWeather)
                .flatMap(Collection::stream)
                .toList();
    }

    private static boolean determineIsWindy(double maxWind) {
        return maxWind > MIN_WINDY_VALUE;
    }

    private static List<Precipitation> prepareListOfPrecipitations(boolean willItRain, boolean willItSnow) {
        List<Precipitation> precipitations = new ArrayList<>();
        if (willItRain) {
            precipitations.add(RAIN);
        }
        if (willItSnow) {
            precipitations.add(SNOW);
        }
        if (precipitations.isEmpty()) {
            precipitations.add(CLEAR);
        }
        return precipitations;
    }
}
