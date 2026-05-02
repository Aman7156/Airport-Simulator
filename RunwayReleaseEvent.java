import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RunwayReleaseEvent extends EventBase {

    private final Runway runway;
    private final Aircraft aircraft;
    private final boolean isLanding;

    public RunwayReleaseEvent(double time, Runway runway, Aircraft aircraft, boolean isLanding) {
        super(time);
        this.runway = runway;
        this.aircraft = aircraft;
        this.isLanding = isLanding;
    }

    @Override
    public void execute(Simulator sim) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        System.out.printf("[%s] Runway %s released by %s (%s)%n",
                LocalTime.now().format(formatter),
                runway.getId(),
                aircraft != null ? aircraft.getId() : "N/A",
                isLanding ? "Landing" : "Takeoff");

        sim.getStats().recordRunwayFree(runway.getId(), getTime());

        runway.free();
        if (!isLanding && aircraft != null) {
            aircraft.takeoff();
        }

        sim.getScheduler().onRunwayReleased(runway, getTime(), aircraft, isLanding);
    }
}
