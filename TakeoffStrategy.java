public interface TakeoffStrategy {
    double computeTakeoffDuration(Aircraft aircraft, Weather weather);
}
