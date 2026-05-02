public class PassengerTakeoffStrategy implements TakeoffStrategy {
    @Override
    public double computeTakeoffDuration(Aircraft aircraft, Weather weather) {
        return 3.0 + weather.getTakeoffDelayFactor();
    }
}
