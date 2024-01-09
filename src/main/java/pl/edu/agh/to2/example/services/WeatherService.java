package pl.edu.agh.to2.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.example.converters.WeatherResponseConverter;
import pl.edu.agh.to2.example.exceptions.MissingDataException;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherForecastResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherHistoryResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;
import pl.edu.agh.to2.example.utils.ResponseHolder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class WeatherService {
    private static final String URL_FORECAST = "http://api.weatherapi.com/v1/forecast.json";
    private static final String URL_HISTORY = "http://api.weatherapi.com/v1/history.json";
    private static final String API_KEY = "53416f14f51041f593a122744232711";
    private static final String LOCATION = "location";
    private static final String FORECAST = "forecast";
    private final OkHttpClient client;
    private final Gson gson;

    @Autowired
    public WeatherService(OkHttpClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public Optional<WeatherResponseConverted> findWeatherForecast(List<WeatherRequest> weatherRequests) throws IOException {
        List<WeatherForecastResponse> responses = new ArrayList<>();
        for (WeatherRequest weatherRequest : weatherRequests) {
            Request request = createHttpForecastRequest(weatherRequest);

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    WeatherForecastResponse valuesFromJson = getValuesFromJson(response);
                    responses.add(valuesFromJson);
                }
            } catch (IOException e) {
                throw new IOException();
            }
        }
        if (responses.isEmpty()) {
            return Optional.empty();
        }
        List<Integer> wasRainyFirstDay = new ArrayList<>();
        List<Integer> wasRainySecondDay = new ArrayList<>();
        LocalDate date = LocalDate.now();

        for (WeatherRequest weatherRequest : weatherRequests) {
            Request request = createHttpHistoryRequest(weatherRequest, date.minusDays(2));
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    int wasRainy = getRainInformation(response);
                    wasRainyFirstDay.add(wasRainy);
                }
            }
            request = createHttpHistoryRequest(weatherRequest, date.minusDays(1));
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    int wasRainy = getRainInformation(response);
                    wasRainySecondDay.add(wasRainy);
                }
            }
        }
        WeatherHistoryResponse historyResponse = new WeatherHistoryResponse(wasRainyFirstDay, wasRainySecondDay);

        try {
            ResponseHolder.updateLastResponse(WeatherResponseConverter.convertWeatherResponse(responses, historyResponse));
            return Optional.of(WeatherResponseConverter.convertWeatherResponse(responses, historyResponse));
        } catch (MissingDataException e) {
            return Optional.empty();
        }
    }

    private Request createHttpForecastRequest(WeatherRequest weatherRequest) {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_FORECAST)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params)
                .addQueryParameter("days", "2");
        return new Request.Builder()
                .url(builder.build())
                .build();
    }

    private Request createHttpHistoryRequest(WeatherRequest weatherRequest, LocalDate date) {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        String dt = date.toString();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_HISTORY)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params)
                .addQueryParameter("dt", dt);
        return new Request.Builder()
                .url(builder.build())
                .build();
    }

    private WeatherForecastResponse getValuesFromJson(Response response) throws IOException {
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        String location = json.getAsJsonObject(LOCATION).get("name").getAsString();

        JsonArray weathers = json.getAsJsonObject(FORECAST).getAsJsonArray("forecastday");

        JsonArray todayWeatherJson = weathers.get(0).getAsJsonObject().getAsJsonArray("hour");
        WeatherPerHour[] weatherPerHours = gson.fromJson(todayWeatherJson, WeatherPerHour[].class);
        List<WeatherPerHour> todayWeather = List.of(weatherPerHours);

        JsonArray tomorrowWeatherJson = weathers.get(1).getAsJsonObject().getAsJsonArray("hour");
        List<WeatherPerHour> tomorrowWeather = List.of(gson.fromJson(tomorrowWeatherJson, WeatherPerHour[].class));

        return new WeatherForecastResponse(location, todayWeather, tomorrowWeather);
    }

    private int getRainInformation(Response response) throws IOException {
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        return json.getAsJsonObject(FORECAST)
                .getAsJsonArray("forecastday")
                .get(0).getAsJsonObject().
                getAsJsonObject("day").get("daily_will_it_rain").getAsInt();
    }
}
