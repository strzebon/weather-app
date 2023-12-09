package pl.edu.agh.to2.example.models.weather;

import java.util.ArrayList;
import java.util.List;

public enum Precipitation {
    SNOW,
    RAIN,
    NONE;

    public static List<Precipitation> prepareListOfPrecipitations(boolean willItRain, boolean willItSnow) {
        List<Precipitation> precipitations = new ArrayList<>();
        if (willItRain) {
            precipitations.add(RAIN);
        }
        if (willItSnow) {
            precipitations.add(SNOW);
        }
        if (precipitations.isEmpty()) {
            precipitations.add(NONE);
        }
        return precipitations;
    }
}
