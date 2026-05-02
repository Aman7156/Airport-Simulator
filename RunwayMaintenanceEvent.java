public class RunwayMaintenanceEvent extends EventBase {

    private final Runway runway;
    private final double duration;

    public RunwayMaintenanceEvent(double time, Runway runway, double duration) {
        super(time);
        this.runway = runway;
        this.duration = duration;
    }

    @Override
    public void execute(Simulator sim) {

        
        runway.setState(new MaintenanceState());

        System.out.printf(
                "[%.2f] Runway %s closed for maintenance (%.2f time units)%n",
                getTime(),
                runway.getId(),
                duration
        );

        
        sim.scheduleEvent(
                new RunwayMaintenanceEndEvent(getTime() + duration, runway)
        );
    }
}
