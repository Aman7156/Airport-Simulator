import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class WeatherEvent extends EventBase {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final WeatherStation station;

    public WeatherEvent(double time, WeatherStation station) {
        super(time);
        this.station = station;
    }

    @Override
    public void execute(Simulator sim) {

        station.updateWeather();
        Weather w = station.getWeather();

        System.out.printf("[%s] Weather changed: %s (landing delay=%.2f)%n",
                LocalTime.now().format(FORMATTER),
                w.getCondition(),
                w.getLandingDelayFactor());

        double next = getTime() + 50 + sim.getRng().nextDouble() * 50;

        if (next <= sim.getEndTime()) {
            sim.scheduleEvent(new WeatherEvent(next, station));
        }
    }
}
