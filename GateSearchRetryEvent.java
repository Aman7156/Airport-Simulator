public class GateSearchRetryEvent extends EventBase {

    private final Aircraft aircraft;

    public GateSearchRetryEvent(double time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void execute(Simulator sim) {
        System.out.printf("[%.2f] Retrying gate assignment for %s%n",
                getTime(), aircraft.getId());

        sim.getScheduler().handleLandingRequest(aircraft, getTime());
    }
}
