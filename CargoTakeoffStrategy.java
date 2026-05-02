public class CargoTakeoffStrategy implements TakeoffStrategy {
    @Override
    public double computeTakeoffDuration(Aircraft aircraft, Weather weather) {
        return 4.0 + weather.getTakeoffDelayFactor();
    }
}

