public class TakeoffRequestEvent extends EventBase {

    private final Aircraft aircraft;

    public TakeoffRequestEvent(double time, Aircraft aircraft) {
        super(time);
        this.aircraft = aircraft;
    }

    @Override
    public void execute(Simulator sim) {
        sim.getScheduler().handleTakeoffRequest(aircraft, getTime());
    }
}
