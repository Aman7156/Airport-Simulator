public class RunwayMaintenanceEndEvent extends EventBase {

    private final Runway runway;

    public RunwayMaintenanceEndEvent(double time, Runway runway) {
        super(time);
        this.runway = runway;
    }

    @Override
    public void execute(Simulator sim) {

        runway.setState(new AvailableState());

        System.out.printf(
                "[%.2f] Runway %s maintenance complete, now AVAILABLE%n",
                getTime(),
                runway.getId()
        );
    }
}
