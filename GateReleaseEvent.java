import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GateReleaseEvent extends EventBase {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private final Gate gate;         
    private final Aircraft aircraft;

    public GateReleaseEvent(double time, Gate gate, Aircraft aircraft) {
        super(time);
        this.gate = gate;
        this.aircraft = aircraft;
    }

    @Override
    public void execute(Simulator sim) {

        gate.release();

        System.out.printf("[%s] Gate %s released by %s%n",
                LocalTime.now().format(FORMATTER),
                gate.getId(),
                aircraft.getId());

        aircraft.readyForTakeoff();

        sim.scheduleEvent(new TakeoffRequestEvent(getTime() + 1.0, aircraft));
    }
}
