package pl.edu.agh.to2.example.services;

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
import pl.edu.agh.to2.example.models.weather.request.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponse;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @Mock
    private Call call;
    @Mock
    private ResponseBody responseBody;
    @Mock
    private JsonObject json;
    @Mock
    private Response response;
    @Mock
    private JsonObject locationObject, currentDeeperObject, currentObject;
    @Mock
    private JsonElement conditionElement, jsonElement, precipElement, imgElement, tempElement, locationElement;

    private static final String LOCATION_1 = "London";
    private static final double TEMP = 5.0;
    private static final String IMG_PATH = "/img/path";
    private static final String SOME_CONDITION = "Some condition";
    private static final double PRECIP = 3.0;

    private static final String LOCATION = "location";
    private static final String CURRENT = "current";
    private static final String CONDITION = "condition";

    @Test
    void shouldReturnEmptyWhenNewCallIsNull() throws Exception {
        //given
        when(okHttpClient.newCall(any())).thenReturn(null);

        //when
        Optional<WeatherResponseConverted> response = service.findWeatherForecast(List.of(new WeatherRequest(1, 1)));

        //then
        assertTrue(response.isEmpty());

    }

    @Test
    void shouldReturnNonemptyWhenRightCoordinates() throws Exception {
        //given
        String[] imgPathConverted = IMG_PATH.split("/");
        String img_path = imgPathConverted[imgPathConverted.length - 2] + "/" + imgPathConverted[imgPathConverted.length - 1];

        applyMocks();

        Optional<WeatherResponse> finalResponse;
        try (MockedStatic<JsonParser> jsonParserMocked = Mockito.mockStatic(JsonParser.class)) {
            //given
            jsonParserMocked.when(() -> JsonParser.parseString(any())).thenReturn(jsonElement);
            jsonParserMocked.when(() -> JsonParser.parseString(any()).getAsJsonObject()).thenReturn(json);

            //when
            finalResponse = service.findWeather(new WeatherRequest(1, 1));
        }

        //then
        assertTrue(finalResponse.isPresent());
        WeatherResponse weatherResponse = finalResponse.get();
        assertEquals(LOCATION_1, weatherResponse.location());
        assertEquals(TEMP, weatherResponse.temp_c());
        assertEquals(img_path, weatherResponse.img_path());
        assertEquals(SOME_CONDITION, weatherResponse.condition());
        assertEquals(PRECIP, weatherResponse.precip_mm());
    }

    @Test
    void shouldThrowExceptionWhenProblemWithExecute() throws IOException {
        //given
        when(okHttpClient.newCall(any())).thenReturn(call);
        when(okHttpClient.newCall(any()).execute()).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> service.findWeatherForecast(List.of(new WeatherRequest(1, 1))));
    }


    private void applyMocks() throws IOException {
        //given
        when(response.code()).thenReturn(200);
        when(response.body()).thenReturn(responseBody);
        when(okHttpClient.newCall(any())).thenReturn(call);
        when(okHttpClient.newCall(any()).execute()).thenReturn(response);

        when(json.getAsJsonObject(LOCATION)).thenReturn(locationObject);
        when(locationObject.get("name")).thenReturn(locationElement);
        when(locationElement.getAsString()).thenReturn(LOCATION_1);

        when(json.getAsJsonObject(CURRENT)).thenReturn(currentObject);
        when(currentObject.get("temp_c")).thenReturn(tempElement);
        when(tempElement.getAsString()).thenReturn(String.valueOf(TEMP));

        when(json.getAsJsonObject(CURRENT)).thenReturn(currentObject);
        when(currentObject.getAsJsonObject(CONDITION)).thenReturn(currentDeeperObject);
        when(currentDeeperObject.get("icon")).thenReturn(imgElement);
        when(imgElement.getAsString()).thenReturn(IMG_PATH);

        when(json.getAsJsonObject(CURRENT)).thenReturn(currentObject);
        when(currentObject.getAsJsonObject(CONDITION)).thenReturn(currentDeeperObject);
        when(currentDeeperObject.get("text")).thenReturn(conditionElement);
        when(conditionElement.getAsString()).thenReturn(SOME_CONDITION);

        when(json.getAsJsonObject(CURRENT)).thenReturn(currentObject);
        when(currentObject.get("precip_mm")).thenReturn(precipElement);
        when(precipElement.getAsString()).thenReturn(String.valueOf(PRECIP));
    }
}
