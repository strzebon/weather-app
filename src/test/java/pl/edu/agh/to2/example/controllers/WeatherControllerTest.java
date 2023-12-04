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
import pl.edu.agh.to2.example.models.weather.WeatherResponse;
import pl.edu.agh.to2.example.services.WeatherService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
    public void shouldReturnNotFoundWhenServiceStatusNotFound() throws Exception {
        //given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(1, 1);
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeather(any())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnBadGatewayWhenServiceThrowsIOException() throws Exception {
        //given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(1, 1);
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeather(any())).thenThrow(IOException.class);

        mvc.perform(MockMvcRequestBuilders.post("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadGateway());
    }

    @Test
    public void shouldReturnOkWhenServiceStatusOk() throws Exception {
        //given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(1, 1);
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeather(any())).thenReturn(new WeatherResponse("any", 1, "any", "any", 1));

        mvc.perform(MockMvcRequestBuilders.post("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturnNotFoundLastResponse() throws Exception {
        //given
        String requestJson = new ObjectMapper().writeValueAsString(null);
        when(weatherService.getLastResponse()).thenReturn(new WeatherResponse("any", 1, "any", "any", 1));

        mvc.perform(MockMvcRequestBuilders.get("/weather/current").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnLastResponseWhenItExists() throws Exception {
        //given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(1, 1);
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.getLastResponse()).thenReturn(new WeatherResponse("any", 1, "any", "any", 1));
        when(weatherService.isLastResponse()).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.get("/weather/current").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
