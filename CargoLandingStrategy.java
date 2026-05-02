public class CargoLandingStrategy implements LandingStrategy {
    @Override
    public double computeLandingDuration(Aircraft aircraft, Weather weather) {
        return 6.0 + weather.getLandingDelayFactor();
    }
}

