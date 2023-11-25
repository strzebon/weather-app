package pl.edu.agh.to2.example;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.toString());

    public static void main(String[] args) {
        log.info("Hello world");
        SpringApplication.run(Main.class, args);
//		Application.launch(App.class);
    }
}
