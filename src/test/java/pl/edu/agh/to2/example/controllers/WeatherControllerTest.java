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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.edu.agh.to2.example.Main;
import pl.edu.agh.to2.example.models.dto.WeatherRequestDto;
import pl.edu.agh.to2.example.models.weather.WeatherResponse;
import pl.edu.agh.to2.example.services.WeatherService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private WeatherService weatherService;

    @Test
    public void shouldReturnNotFoundWhenNotFound() throws Exception {
        //given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(1, 1);
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeather(any())).thenReturn(null);

        mvc.perform(get("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnOkWhenOk() throws Exception {
        //given
        WeatherRequestDto weatherRequestDto = new WeatherRequestDto(1, 1);
        String requestJson = new ObjectMapper().writeValueAsString(weatherRequestDto);
        when(weatherService.findWeather(any())).thenReturn(new WeatherResponse());

        mvc.perform(get("/weather").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}