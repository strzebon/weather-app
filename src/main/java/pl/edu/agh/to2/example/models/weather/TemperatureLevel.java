package pl.edu.agh.to2.example.models.weather;

public enum TemperatureLevel {
    FREEZING,
    COLD,
    WARM,
    HOT;

    public static TemperatureLevel determineTemperatureLevel(double sensedTemp) {
        if (sensedTemp < -5) {
            return FREEZING;
        }
        if (sensedTemp < 5) {
            return COLD;
        }
        if (sensedTemp < 20) {
            return WARM;
        }
        return HOT;
    }
}
