package pl.edu.agh.to2.example.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to2.example.models.location.Location;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.repositories.TripRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class TripServiceTest {
    @Autowired
    private TripService tripService;

    @MockBean
    private TripRepository tripRepository;

    @Test
    void shouldReturnAllTripsFromDb() {
        //given
        when(tripRepository.findAll()).thenReturn(List.of());

        assertEquals(3, tripService.getTrips().size());
    }

    @Test
    void shouldReturnEmptyWhenWrongId() {
        assertTrue(tripService.getTrip(4).isEmpty());
    }

    @Test
    void shouldReturnProperTripWhenRightId() {
        Optional<Trip> trip = tripService.getTrip(2);
        assertTrue(trip.isPresent());
        assertEquals("trip2", trip.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenWrongName() {
        assertTrue(tripService.getTrip("trip4").isEmpty());
    }

    @Test
    void shouldReturnProperTripWhenRightName() {
        Optional<Trip> trip = tripService.getTrip("trip2");
        assertTrue(trip.isPresent());
        assertEquals(2, trip.get().getId());
    }

    @Test
    void shouldReturnTripWithProperLocationsWhenRightId() {
        Optional<Trip> trip = tripService.getTrip(3);
        assertTrue(trip.isPresent());
        List<Location> locations = trip.get().getLocations();
        assertEquals(3, locations.size());
        assertEquals(4, locations.get(0).getLat());
        assertEquals(4, locations.get(0).getLng());
    }

    @Test
    void whenIdInDbProperTripShouldBeDeleted() {
        //given
        int numberOfTripsInDb = tripService.getTrips().size();

        //when
        tripService.deleteTrip(3);

        //then
        Optional<Trip> deletedTrip = tripService.getTrip(3);
        assertTrue(deletedTrip.isEmpty());

        int numberOfTripsInDbAfterDeleting = tripService.getTrips().size();
        assertEquals(numberOfTripsInDb - 1, numberOfTripsInDbAfterDeleting);
    }

    @Test
    void whenDeletingIdNotInDbNothingShouldHappen() {
        //given
        int numberOfTripsInDb = tripService.getTrips().size();

        //when
        tripService.deleteTrip(4);

        //then
        int numberOfTripsInDbAfterDeleting = tripService.getTrips().size();
        assertEquals(numberOfTripsInDb, numberOfTripsInDbAfterDeleting);
    }

    @Test
    void shouldSaveProperTripInDb() {
        //given
        int numberOfTripsInDb = tripService.getTrips().size();
        String tripName = "Trip4";
        Trip tripToSave = new Trip(tripName);

        //when
        tripService.saveTrip(tripToSave);

        //then
        int numberOfTripsInDbAfterSave = tripService.getTrips().size();
        assertEquals(numberOfTripsInDb + 1, numberOfTripsInDbAfterSave);

        Optional<Trip> savedTrip = tripService.getTrip(tripName);
        assertTrue(savedTrip.isPresent());
    }
}