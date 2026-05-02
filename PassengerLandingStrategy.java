public class PassengerLandingStrategy implements LandingStrategy {
    @Override
    public double computeLandingDuration(Aircraft aircraft, Weather weather) {
        return 4.0 + weather.getLandingDelayFactor();
    }
}

