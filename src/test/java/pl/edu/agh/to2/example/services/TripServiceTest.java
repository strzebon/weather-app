package pl.edu.agh.to2.example.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.edu.agh.to2.example.exceptions.ArgumentToUseInDbIsNullException;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.repositories.TripRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class TripServiceTest {
    @Autowired
    private TripService tripService;

    @MockBean
    private TripRepository tripRepository;

    @Test
    void shouldThrowExceptionWhenFindByIdInRepositoryThrowsException() {
        //given
        int id = -1;
        when(tripRepository.findById(id)).thenThrow(IllegalArgumentException.class);

        assertThrows(ArgumentToUseInDbIsNullException.class, () -> tripService.getTrip(id));
    }

    @Test
    void shouldThrowExceptionWhenSaveInRepositoryThrowsException() {
        //give
        Trip tripToSave = null;
        when(tripRepository.save(tripToSave)).thenThrow(IllegalArgumentException.class);

        assertThrows(ArgumentToUseInDbIsNullException.class, () -> tripService.saveTrip(tripToSave));
    }

    @Test
    void shouldThrowExceptionWhenDeletingInRepositoryThrowsException() {
        //give
        int id = -1;
        Trip trip = new Trip();
        when(tripRepository.findById(id)).thenReturn(Optional.of(trip));
        doThrow(new IllegalArgumentException()).when(tripRepository).delete(trip);

        assertThrows(ArgumentToUseInDbIsNullException.class, () -> tripService.deleteTrip(id));
    }
}