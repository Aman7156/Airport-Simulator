public class EmergencyLandingStrategy implements LandingStrategy {
    @Override
    public double computeLandingDuration(Aircraft aircraft, Weather weather) {
        return 2.0; 
    }
}
