package pl.edu.agh.to2.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.example.exceptions.ArgumentToUseInDbIsNullException;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.repositories.TripRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> getTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTrip(int id) throws ArgumentToUseInDbIsNullException {
        try {
            return tripRepository.findById(id);
        } catch (IllegalArgumentException ignored) {
            throw new ArgumentToUseInDbIsNullException();
        }
    }

    public Trip saveTrip(Trip trip) throws ArgumentToUseInDbIsNullException {
        try {
            return tripRepository.save(trip);
        } catch (IllegalArgumentException ignored) {
            throw new ArgumentToUseInDbIsNullException();
        }
    }

    public void deleteTrip(int id) throws ArgumentToUseInDbIsNullException {
        Optional<Trip> trip = getTrip(id);
        try {
            trip.ifPresent(tripRepository::delete);
        } catch (IllegalArgumentException ignored) {
            throw new ArgumentToUseInDbIsNullException();
        }
    }
}
