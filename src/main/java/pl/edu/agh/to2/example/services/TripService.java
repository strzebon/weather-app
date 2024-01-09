package pl.edu.agh.to2.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Optional<Trip> getTrip(int id) throws IllegalArgumentException{
        return tripRepository.findById(id);
    }

    public Optional<Trip> getTrip(String name) {
        return tripRepository.findByName(name);
    }

    public Trip saveTrip(Trip trip) throws IllegalArgumentException{
        return tripRepository.save(trip);
    }

    public void deleteTrip(int id) throws IllegalArgumentException {
        Optional<Trip> trip = this.getTrip(id);
        trip.ifPresent(tripRepository::delete);
    }
}
