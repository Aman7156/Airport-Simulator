public class ForceLandingCommand implements ATCCommand {

    private final Scheduler scheduler;
    private final Aircraft aircraft;
    private final double time;
    
    public ForceLandingCommand(Scheduler scheduler, Aircraft aircraft, double time) {
        this.scheduler = scheduler;
        this.aircraft = aircraft;
        this.time = time;
    }

    @Override
    public void execute() {
        System.out.println(" ATC COMMAND: Force landing for " + aircraft.getId());
        scheduler.handleEmergencyLanding(aircraft, time);
    }
}
