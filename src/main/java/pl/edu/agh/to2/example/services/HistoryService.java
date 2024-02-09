package pl.edu.agh.to2.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.example.exceptions.CallToApiWentWrongException;
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherHistoryResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static pl.edu.agh.to2.example.services.ForecastService.API_KEY;

@Service
public class HistoryService {
    private static final String URL_HISTORY = "http://api.weatherapi.com/v1/history.json";
    private static final String FORECAST = "forecast";
    private final OkHttpClient client;

    @Autowired
    public HistoryService(OkHttpClient client, Gson gson) {
        this.client = client;
    }

    public WeatherHistoryResponse findWeatherHistory(List<WeatherRequest> weatherRequests) throws CallToApiWentWrongException {
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
            } catch (IOException ignored) {
                throw new CallToApiWentWrongException();
            }
            request = createHttpHistoryRequest(weatherRequest, date.minusDays(1));
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    int wasRainy = getRainInformation(response);
                    wasRainySecondDay.add(wasRainy);
                }
            } catch (IOException ignored) {
                throw new CallToApiWentWrongException();
            }
        }
        return new WeatherHistoryResponse(wasRainyFirstDay, wasRainySecondDay);
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

    private int getRainInformation(Response response) throws IOException {
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        return json.getAsJsonObject(FORECAST)
                .getAsJsonArray("forecastday")
                .get(0).getAsJsonObject().
                getAsJsonObject("day").get("daily_will_it_rain").getAsInt();
    }
}
