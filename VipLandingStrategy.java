public class VipLandingStrategy implements LandingStrategy {
    @Override
    public double computeLandingDuration(Aircraft aircraft, Weather weather) {
        return 3.0 + weather.getLandingDelayFactor();
    }
}

