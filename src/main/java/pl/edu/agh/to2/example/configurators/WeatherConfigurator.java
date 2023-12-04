package pl.edu.agh.to2.example.configurators;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConfigurator {

    @Bean
    OkHttpClient client() {
        return new OkHttpClient();
    }
}
