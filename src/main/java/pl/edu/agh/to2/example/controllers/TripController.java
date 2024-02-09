package pl.edu.agh.to2.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.example.exceptions.ArgumentToUseInDbIsNullException;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.services.TripService;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin()
@RequestMapping("")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/trips")
    public ResponseEntity<Trip> saveNewTrip(@RequestBody Trip trip) {
        Trip savedTrip;
        try {
            savedTrip = tripService.saveTrip(trip);
        } catch (ArgumentToUseInDbIsNullException ignored) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(savedTrip, OK);
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable int id) {
        Optional<Trip> trip;
        try {
            trip = tripService.getTrip(id);
        } catch (ArgumentToUseInDbIsNullException ignored) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return trip
                .map(foundTrip -> new ResponseEntity<>(foundTrip, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/trips")
    public ResponseEntity<List<Trip>> getAllTrips() {
        return new ResponseEntity<>(tripService.getTrips(), OK);
    }

    @DeleteMapping("/trips/{id}")
    public ResponseEntity<HttpStatus> deleteTripById(@PathVariable int id) {
        try {
            tripService.deleteTrip(id);
        } catch (ArgumentToUseInDbIsNullException ignored) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(OK);
    }
}
