package pl.edu.agh.to2.example.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.edu.agh.to2.example.repositories.TripRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class TripServiceTest {
    @Autowired
    private TripService tripService;

    @MockBean
    private TripRepository tripRepository;

    @Test
    void shouldNothingHappenWhenDeletingIdNotInDb() {
        //given
        when(tripRepository.findById(4)).thenReturn(Optional.empty());

        //when
        tripService.deleteTrip(4);

        //then
        assertTrue(true);
    }
}