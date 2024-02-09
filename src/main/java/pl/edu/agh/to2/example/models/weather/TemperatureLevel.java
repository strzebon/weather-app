package pl.edu.agh.to2.example.models.weather;

public enum TemperatureLevel {
    FREEZING,
    COLD,
    CHILLY,
    WARM,
    HOT;

    public static TemperatureLevel determineTemperatureLevel(double sensedTemp) {
        if (sensedTemp < -3) {
            return FREEZING;
        }
        if (sensedTemp < 3) {
            return COLD;
        }
        if (sensedTemp < 10) {
            return CHILLY;
        }
        if (sensedTemp < 20) {
            return WARM;
        }
        return HOT;
    }
}
