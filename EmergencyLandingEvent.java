public class EmergencyLandingEvent extends EventBase {

    private final Aircraft aircraft;

    public EmergencyLandingEvent(double time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void execute(Simulator sim) {
        System.out.printf("[%.2f] EMERGENCY landing triggered for %s%n",
                getTime(), aircraft.getId());

        sim.getStats().countEmergency();
        sim.getScheduler().handleEmergencyLanding(aircraft, getTime());
    }
}
