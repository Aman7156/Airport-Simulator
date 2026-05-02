import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LandingRequestEvent extends EventBase {

    private final Aircraft aircraft;
    private static final DateTimeFormatter realtimeFmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public LandingRequestEvent(double time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void execute(Simulator sim) {

       
        String now = LocalTime.now().format(realtimeFmt);
        System.out.printf("[%s] Landing request: %s (type=%s)\n",
                now, aircraft.getId(), aircraft.getType());
                sim.getStats().countArrivalRequest();


        double delay = sim.getWeather().getLandingDelayFactor();
        double scheduledTime = getTime() + delay;

        sim.getScheduler().handleLandingRequest(aircraft, scheduledTime);

        if (aircraft.isEmergency()) {
            sim.scheduleEvent(new EmergencyLandingEvent(getTime() + 0.1, aircraft));
        }
    }
}



