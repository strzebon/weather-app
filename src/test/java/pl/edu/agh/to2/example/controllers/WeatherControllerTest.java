package pl.edu.agh.to2.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.edu.agh.to2.example.Main;
import pl.edu.agh.to2.example.models.dto.WeatherRequestDto;
import pl.edu.agh.to2.example.models.weather.Precipitation;
import pl.edu.agh.to2.example.models.weather.response.WeatherResponseConverted;
import pl.edu.agh.to2.example.services.WeatherService;
import pl.edu.agh.to2.example.utils.ResponseHolder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static pl.edu.agh.to2.example.models.weather.TemperatureLevel.COLD;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@AutoConfigureMockMvc
class WeatherControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private WeatherService weatherService;

    @Test
    void shouldReturnNotFoundWhenServiceStatusNotFound() throws Exception {
        //given
        List<WeatherRequestDto> weatherRequestDto = List.of(new WeatherRequestDto(1, 1));
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeatherForecast(any())).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadGatewayWhenServiceThrowsIOException() throws Exception {
        //given
        List<WeatherRequestDto> weatherRequestDto = List.of(new WeatherRequestDto(1, 1));
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeatherForecast(any())).thenThrow(IOException.class);

        mvc.perform(MockMvcRequestBuilders.post("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadGateway());
    }

    @Test
    void shouldReturnOkWhenServiceStatusOk() throws Exception {
        //given
        List<WeatherRequestDto> weatherRequestDto = List.of(new WeatherRequestDto(1, 1));
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeatherForecast(any())).thenReturn(Optional.of(
                new WeatherResponseConverted(emptyList(), COLD, true, List.of(Precipitation.CLEAR), 1, 1, 1)));

        mvc.perform(MockMvcRequestBuilders.post("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturnNotFoundLastResponse() throws Exception {
        //given
        ResponseHolder.updateLastResponse(null);

        mvc.perform(MockMvcRequestBuilders.get("/weather/current"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnLastResponseWhenItExists() throws Exception {
        //given
        ResponseHolder.updateLastResponse(new WeatherResponseConverted(List.of("any"), COLD, true, List.of(Precipitation.CLEAR),
                1.0, 2.0, 3.0));

        mvc.perform(MockMvcRequestBuilders.get("/weather/current"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
