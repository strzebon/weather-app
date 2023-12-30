package pl.edu.agh.to2.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to2.example.models.trip.Trip;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    Optional<Trip> findByName(String name);
}
