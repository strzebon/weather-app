package pl.edu.agh.to2.example.repositiories;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to2.example.models.location.Location;
import pl.edu.agh.to2.example.models.trip.Trip;
import pl.edu.agh.to2.example.repositories.TripRepository;

import java.util.List;

@Configuration
public class TripDbConfigurator {
    @Bean
    CommandLineRunner commandLineRunner(TripRepository tripRepository) {
        return args -> {
            if (tripRepository.count() == 0) {
                Location location1 = new Location(1, 1);
                Location location2 = new Location(2, 2);
                Location location3 = new Location(3, 3);
                Location location4 = new Location(4, 4);
                Location location5 = new Location(5, 5);
                Location location6 = new Location(6, 6);

                Trip trip1 = new Trip("trip1");
                Trip trip2 = new Trip("trip2");
                Trip trip3 = new Trip("trip3");

                trip1.addLocation(location1);
                trip2.addLocation(location2);
                trip2.addLocation(location3);
                trip3.addLocation(location4);
                trip3.addLocation(location5);
                trip3.addLocation(location6);

                tripRepository.saveAll(List.of(trip1, trip2, trip3));
            }
        };
    }
}
