package pl.edu.agh.to2.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import pl.edu.agh.to2.example.exceptions.ArgumentToUseInDbIsNullException;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.services.TripService;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@AutoConfigureMockMvc
class TripControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TripService tripService;

    @SneakyThrows
    @Test
    void shouldReturnBadRequestWhenSavingInServiceFails() {
        //given
        String requestJson = new ObjectMapper().writeValueAsString(null);
        given(tripService.saveTrip(any())).willAnswer(invocation -> {
            throw new ArgumentToUseInDbIsNullException();
        });

        mvc.perform(MockMvcRequestBuilders.post("/trips").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturnOKWhenSuccessfulSavingInService() {
        //given
        Trip requestTrip = new Trip("Trip1");
        String requestJson = new ObjectMapper().writeValueAsString(requestTrip);

        mvc.perform(MockMvcRequestBuilders.post("/trips").contentType(APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    void shouldReturnBadRequestWhenFindingByIdInServiceFails() {
        //given
        int id = 0;
        when(tripService.getTrip(id)).thenThrow(ArgumentToUseInDbIsNullException.class);

        mvc.perform(MockMvcRequestBuilders.get("/trips/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturnOkWhenSuccessfulFindingByIdInService() {
        //given
        Trip trip = new Trip("Trip1");
        int id = 1;
        when(tripService.getTrip(id)).thenReturn(Optional.of(trip));

        mvc.perform(MockMvcRequestBuilders.get("/trips/{id}", id)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    void shouldReturnNotFoundWhenNotFoundByIdInService() {
        //given
        int id = 1;
        when(tripService.getTrip(id)).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/trips/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldReturnOkWhenSuccessfulFindingAllTripsInService() {
        //given
        when(tripService.getTrips()).thenReturn(emptyList());

        mvc.perform(MockMvcRequestBuilders.get("/trips")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    void shouldReturnBadRequestWhenDeletingTripFails() {
        //given
        int id = 0;
        doThrow(ArgumentToUseInDbIsNullException.class).when(tripService).deleteTrip(id);
//        willAnswer( invocation -> { throw new CallToApiWentWrongException(); }).given(tripService).deleteTrip(id);


        mvc.perform(MockMvcRequestBuilders.delete("/trips/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldReturnOKWhenSuccessfulDeletingTrip() {
        //given
        int id = 0;

        mvc.perform(MockMvcRequestBuilders.delete("/trips/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}