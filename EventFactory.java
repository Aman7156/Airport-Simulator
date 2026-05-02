public class EventFactory {

    public static Event createLandingEvent(double time, Aircraft ac) {
        return new LandingRequestEvent(time, ac);
    }

    public static Event createTakeoffEvent(double time, Aircraft ac) {
        return new TakeoffRequestEvent(time, ac);
    }

    public static Event createEmergencyEvent(double time, Aircraft ac) {
        return new EmergencyLandingEvent(time, ac);
    }

    public static Event createWeatherEvent(double time, WeatherStation station) {
        return new WeatherEvent(time, station);
    }
}
