package pl.edu.agh.to2.example.services;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.edu.agh.to2.example.Main;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;

import java.io.IOException;
import java.util.Optional;

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

    @Test
    void shouldReturnEmptyWhenNewCallIsNull() throws Exception {
        //given
        when(okHttpClient.newCall(any())).thenReturn(null);

        //when
        Optional<WeatherResponse> response = service.findWeather(new WeatherRequest(1, 1));

        //then
        assertTrue(response.isEmpty());

    }

//    @Test
//    void shouldReturnNonemptyWhenRightCoordinates() throws Exception {
//
//        //when
//        Optional<WeatherResponse> response = service.findWeather(new WeatherRequest(1, 1));
//
//        //then
//        assertTrue(response.isPresent());
//    }

    @Test
    void shouldThrowExceptionWhenProblemWithExecute() throws IOException {
        //given
        when(okHttpClient.newCall(any())).thenReturn(call);
        when(okHttpClient.newCall(any()).execute()).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> service.findWeather(new WeatherRequest(1, 1)));
    }
}
