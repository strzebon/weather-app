package pl.edu.agh.to2.example.repositiories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.to2.example.models.location.Location;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.repositories.TripRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class TripRepositoryTest {
    @Autowired
    private TripRepository tripRepository;

    @Test
    void shouldReturnNumberOfAllTripsInDb() {
        //when
        long numberOfTrips = tripRepository.count();

        //then
        assertEquals(3, numberOfTrips);
    }

    @Test
    void shouldReturnEmptyWhenWrongId() {
        //when
        Optional<Trip> trip = tripRepository.findById(4);

        //then
        assertTrue(trip.isEmpty());
    }

    @Test
    void shouldReturnProperTripWhenRightId() {
        //when
        Optional<Trip> trip = tripRepository.findById(2);

        //then
        assertTrue(trip.isPresent());
        assertEquals("trip2", trip.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenWrongName() {
        //when
        Optional<Trip> trip = tripRepository.findByName("trip4");

        //then
        assertTrue(trip.isEmpty());
    }

    @Test
    void shouldReturnProperTripWhenRightName() {
        //when
        Optional<Trip> trip = tripRepository.findByName("trip2");

        //then
        assertTrue(trip.isPresent());
        assertEquals(2, trip.get().getId());
    }

    @Test
    void shouldReturnTripWithProperLocationsWhenRightId() {
        //when
        Optional<Trip> trip = tripRepository.findById(3);
        assertTrue(trip.isPresent());
        List<Location> locations = trip.get().getLocations();

        //then
        assertEquals(3, locations.size());
        assertEquals(4, locations.get(0).getLat());
        assertEquals(4, locations.get(0).getLng());
    }

    @Test
    void shouldDeleteProperTripWhenIdInDb() {
        //given
        long numberOfTripsInDb = tripRepository.count();
        Optional<Trip> trip = tripRepository.findById(3);

        //when
        trip.ifPresent(tripRepository::delete);

        //then
        Optional<Trip> deletedTrip = tripRepository.findById(3);
        assertTrue(deletedTrip.isEmpty());

        long numberOfTripsInDbAfterDeleting = tripRepository.count();
        assertEquals(numberOfTripsInDb - 1, numberOfTripsInDbAfterDeleting);
    }

    @Test
    void shouldSaveProperTripInDb() {
        //given
        long numberOfTripsInDb = tripRepository.count();
        String tripName = "trip4";
        Trip tripToSave = new Trip(tripName);

        //when
        tripRepository.save(tripToSave);

        //then
        long numberOfTripsInDbAfterSave = tripRepository.count();
        assertEquals(numberOfTripsInDb + 1, numberOfTripsInDbAfterSave);

        Optional<Trip> savedTrip = tripRepository.findByName(tripName);
        assertTrue(savedTrip.isPresent());
    }
}