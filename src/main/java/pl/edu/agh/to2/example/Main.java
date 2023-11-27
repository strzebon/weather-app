package pl.edu.agh.to2.example;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.to2.example.models.weather.WeatherRequest;
import pl.edu.agh.to2.example.services.WeatherService;

import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.toString());

    public static void main(String[] args) throws IOException {
        log.info("Hello world");
        SpringApplication.run(Main.class, args);
//		Application.launch(App.class);
    }
}
