package pl.edu.agh.to2.example.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;
import pl.edu.agh.to2.example.utils.ResponseHolder;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class WeatherService {

    private static final String URL = "http://api.weatherapi.com/v1/current.json";
    private static final String API_KEY = "53416f14f51041f593a122744232711";
    private static final String LOCATION = "location";
    private static final String CURRENT = "current";
    private static final String CONDITION = "condition";
    private final OkHttpClient client;

    @Autowired
    public WeatherService(OkHttpClient client) {
        this.client = client;
    }

    public Optional<WeatherResponse> findWeather(WeatherRequest weatherRequest) throws IOException {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                String location = json.getAsJsonObject(LOCATION).get("name").getAsString();
                double temp_c = Double.parseDouble(json.getAsJsonObject(CURRENT).get("temp_c").getAsString());
                String[] img_path = json.getAsJsonObject(CURRENT).getAsJsonObject(CONDITION).get("icon").getAsString().split("/");
                String condition = json.getAsJsonObject(CURRENT).getAsJsonObject(CONDITION).get("text").getAsString();
                double precip_mm = Double.parseDouble(json.getAsJsonObject(CURRENT).get("precip_mm").getAsString());
                WeatherResponse lastResponse = new WeatherResponse(location, temp_c, img_path[img_path.length - 2]
                        + "/" + img_path[img_path.length - 1], condition, precip_mm);
                ResponseHolder.updateLastResponse(lastResponse);
                return Optional.of(lastResponse);
            }
        } catch (IOException e) {
            throw new IOException();
        } catch (NullPointerException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}
