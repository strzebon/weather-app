package pl.edu.agh.to2.example.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.to2.example.Main;
import pl.edu.agh.to2.example.models.weather.WeatherPerHour;
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
class WeatherServiceTest {
    @Autowired
    private WeatherService service;
    @MockBean
    private OkHttpClient okHttpClient;
    @MockBean
    private Gson gson;
    @Mock
    private Call call;
    @Mock
    private ResponseBody responseBody;
    @Mock
    private Response response;
    @Mock
    private JsonArray weathers, todayWeatherJsonArray;
    @Mock
    private JsonObject json, locationObject, currentObject, weatherObject;
    @Mock
    private JsonElement jsonElement, locationElement, weatherElement;

    private static final String LOCATION_1 = "London";
    private static final double TEMP = 5.0;
    private static final double PRECIP = 3.0;
    private static final double WIND = 2.0;
    private static final int FLAG_TRUE = 1;

    private static final String LOCATION = "location";
    private static final String FORECAST = "forecast";

    @Test
    void shouldThrowExceptionWhenProblemWithExecute() throws IOException {
        //given
        when(okHttpClient.newCall(any())).thenReturn(call);
        when(okHttpClient.newCall(any()).execute()).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> service.findWeatherForecast(List.of(new WeatherRequest(1, 1))));
    }

    @Test
    void shouldReturnProperValuesInResponseWhenRightCoordinates() throws Exception {
        //given
        int locationSize = 1;

        applyMocks();

        Optional<WeatherResponseConverted> finalResponse;
        try (MockedStatic<JsonParser> jsonParserMocked = Mockito.mockStatic(JsonParser.class)) {
            //given
            jsonParserMocked.when(() -> JsonParser.parseString(any())).thenReturn(jsonElement);
            jsonParserMocked.when(() -> JsonParser.parseString(any()).getAsJsonObject()).thenReturn(json);

            //when
            finalResponse = service.findWeatherForecast(List.of(new WeatherRequest(1, 1)));
        }

        //then
        assertTrue(finalResponse.isPresent());
        WeatherResponseConverted weatherResponse = finalResponse.get();
        assertEquals(locationSize, weatherResponse.locations().size());
        assertEquals(LOCATION_1, weatherResponse.locations().get(0));
        assertEquals(TEMP, weatherResponse.minTemp());
        assertEquals(PRECIP, weatherResponse.maxPrecip());
    }


    private void applyMocks() throws IOException {
        //given
        String hourTitle = "hour";
        WeatherPerHour[] weatherPerHour = new WeatherPerHour[]{
                new WeatherPerHour(null, TEMP, PRECIP, WIND, FLAG_TRUE, FLAG_TRUE)
        };

        when(response.code()).thenReturn(200);
        when(response.body()).thenReturn(responseBody);
        when(okHttpClient.newCall(any())).thenReturn(call);
        when(okHttpClient.newCall(any()).execute()).thenReturn(response);

        when(json.getAsJsonObject(LOCATION)).thenReturn(locationObject);
        when(locationObject.get("name")).thenReturn(locationElement);
        when(locationElement.getAsString()).thenReturn(LOCATION_1);

        when(json.getAsJsonObject(FORECAST)).thenReturn(currentObject);
        when(currentObject.getAsJsonArray("forecastday")).thenReturn(weathers);

        when(weathers.get(anyInt())).thenReturn(weatherElement);
        when(weatherElement.getAsJsonObject()).thenReturn(weatherObject);
        when(weatherObject.getAsJsonArray(hourTitle)).thenReturn(todayWeatherJsonArray);
        when(todayWeatherJsonArray.size()).thenReturn(0);

        when(gson.fromJson(todayWeatherJsonArray, WeatherPerHour[].class)).thenReturn(weatherPerHour);
    }
}
