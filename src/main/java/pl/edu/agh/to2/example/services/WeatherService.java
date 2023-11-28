package pl.edu.agh.to2.example.services;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;

import java.io.IOException;
import java.util.Objects;

@Service
public class WeatherService {

    private WeatherResponse lastResponse;

    private final OkHttpClient client = new OkHttpClient();
    public WeatherResponse findWeather(WeatherRequest weatherRequest) throws IOException {
        String url = "http://api.weatherapi.com/v1/current.json";
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        String apiKey = "53416f14f51041f593a122744232711";
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder()
                .addQueryParameter("key", apiKey)
                .addQueryParameter("q", params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        try(Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                String location = json.getAsJsonObject("location").get("name").getAsString();
                double temp_c = Double.parseDouble(json.getAsJsonObject("current").get("temp_c").getAsString());
                String[] img_path = json.getAsJsonObject("current").getAsJsonObject("condition").get("icon").getAsString().split("/");
                String condition = json.getAsJsonObject("current").getAsJsonObject("condition").get("text").getAsString();
                double precip_mm = Double.parseDouble(json.getAsJsonObject("current").get("precip_mm").getAsString());
                lastResponse = new WeatherResponse(location, temp_c, img_path[img_path.length - 2] + "/" + img_path[img_path.length - 1], condition, precip_mm);
                return lastResponse;
            }
        }
        catch (IOException e) {
            throw new IOException();
        }
        return null;
    }

    public WeatherResponse getLastResponse() {
        return lastResponse;
    }

    public boolean isLastResponse() {
        if (lastResponse == null) {
            return false;
        }
        return true;
    }
}
