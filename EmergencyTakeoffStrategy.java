public class EmergencyTakeoffStrategy implements TakeoffStrategy {
    @Override
    public double computeTakeoffDuration(Aircraft aircraft, Weather weather) {
        return 2.0;
    }
}

