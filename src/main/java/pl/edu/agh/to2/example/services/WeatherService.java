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
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;
import pl.edu.agh.to2.example.utils.ResponseHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WeatherService {
    private static final String URL_FORECAST = "http://api.weatherapi.com/v1/forecast.json";
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
            Request request = createHttpRequest(weatherRequest);

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    WeatherForecastResponse valuesFromJson = getValuesFromJson(response);
//                    todo add to ResponseHolder
                    responses.add(valuesFromJson);
                }
            } catch (IOException e) {
                throw new IOException();
            } catch (NullPointerException ignored) {
                return Optional.empty();
            }
        }
        if (responses.isEmpty()) {
            return Optional.empty();
        }
        try {
            ResponseHolder.updateLastResponse(WeatherResponseConverter.convertWeatherResponse(responses));
            return Optional.of(WeatherResponseConverter.convertWeatherResponse(responses));
        } catch (MissingDataException e) {
            return Optional.empty();
        }
    }

    private Request createHttpRequest(WeatherRequest weatherRequest) {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_FORECAST)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params)
                .addQueryParameter("days", "2");
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
}
