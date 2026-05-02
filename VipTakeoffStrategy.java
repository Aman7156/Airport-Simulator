public class VipTakeoffStrategy implements TakeoffStrategy {
    @Override
    public double computeTakeoffDuration(Aircraft aircraft, Weather weather) {
        return 2.5 + weather.getTakeoffDelayFactor();
    }
}

