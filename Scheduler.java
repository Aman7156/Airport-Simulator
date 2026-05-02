import java.util.*;

public class Scheduler implements WeatherObserver {

    private final ResourceRegistry<Runway> runwayRegistry = new ResourceRegistry<>();
    private final ResourceRegistry<Gate> gateRegistry = new ResourceRegistry<>();

    private final Queue<Aircraft> emergencyQueue = new LinkedList<>(); 
    private final PriorityQueue<Aircraft> landingQueue = new PriorityQueue<>();
    private final Queue<Aircraft> takeoffQueue = new LinkedList<>();

    private final Simulator simulator;
    private final Random localRng;
    private final double nightCurfewStart;
    private double emergProb;
    private double vipProb;

    private boolean takeoffBlocked; //false 

    private final AircraftFactory aircraftFactory;

    public Scheduler(int runwaysCount, int gatesCount, Simulator simulator) {
        this.simulator = simulator;
        localRng = simulator.getRng();
        nightCurfewStart = simulator.getNightCurfewStart();

        for (int i = 1; i <= runwaysCount; i++) {
            runwayRegistry.add(new Runway(String.valueOf(i)));
        }

        for (int i = 1; i <= gatesCount; i++) {
            gateRegistry.add(new Gate("G" + i));
        }

        aircraftFactory = new AircraftFactory(
                simulator.getRng(), emergProb, vipProb);
    }

    public List<Runway> getRunways() { return runwayRegistry.getAll(); }
    public List<Gate> getGates() { return gateRegistry.getAll(); }

    public void setEmergProb(double p) {
        emergProb = p;
        aircraftFactory.setEmergencyProb(p);
    }

    public void setVipProb(double p) {
        vipProb = p;
        aircraftFactory.setVipProb(p);
    }

    public Aircraft createAircraft() {
        return aircraftFactory.createAircraft();
    }

    @Override
    public void onWeatherUpdate(Weather weather) {
        simulator.logReal("Scheduler updated due to new weather: " + weather.getCondition());

        if (weather.isStorm()) {
            takeoffBlocked = true;
            simulator.logReal(" TAKEOFF BLOCKED due to stormy conditions!");
        } else {
            takeoffBlocked = false;
            simulator.logReal(" Takeoffs allowed — weather cleared.");
            processQueues();
        }
    }
    private void processQueues() {
        for (Runway r : runwayRegistry.getAll()) {
            if (r.isAvailable()) {
                
                if (!emergencyQueue.isEmpty()) {
                    assignRunwayForLanding(emergencyQueue.poll(), r, simulator.getCurrentTime(), true);
                
                } else if (!landingQueue.isEmpty()) {
                    assignRunwayForLanding(landingQueue.poll(), r, simulator.getCurrentTime(), false);
                
                } else if (!takeoffBlocked && !takeoffQueue.isEmpty()) {
                    Aircraft next = takeoffQueue.peek();
                    if (canAssignTakeoff(next)) {
                        takeoffQueue.poll();
                        assignRunwayForTakeoff(next, r, simulator.getCurrentTime());
                    }
                }
            }
        }
    }

    public void handleLandingRequest(Aircraft ac, double time) {
        if (time >= nightCurfewStart) {
            simulator.logReal("Night curfew active - cannot land " + ac.getId());
            simulator.getStats().countRefusedLanding();
            return;
        }
    
        if (ac.isEmergency()) {
            attemptAssignRunwayForEmergency(ac, time);
            return;
        }

        if (landingQueue.isEmpty()) {
            Runway free = findAvailableRunway();
            if (free != null) {
                assignRunwayForLanding(ac, free, time);
                return;
            }
        }
        landingQueue.add(ac);
        simulator.logReal(ac.getId() + " queued for landing. Queue size: " + landingQueue.size());
    }

    private Runway findAvailableRunway() {
        for (Runway r : runwayRegistry.getAll()) {
            if (r.isAvailable()) return r;
        }
        return null;
    }

    private void assignRunwayForLanding(Aircraft ac, Runway runway, double time) {
        assignRunwayForLanding(ac, runway, time, false);
    }

    private void assignRunwayForLanding(Aircraft ac, Runway runway, double time, boolean priorityLanding) {
        if (priorityLanding) {
            simulator.getStats().countEmergency();
            simulator.getStats().countArrivalRequest();
            simulator.logReal("PRIORITY landing for " + ac.getId() + " on runway " + runway.getId());
        } else {
            simulator.logReal(ac.getId() + " assigned to runway " + runway.getId() + " for landing");
        }

        runway.occupy();
        ac.landingAssigned();
        simulator.getStats().recordRunwayUse(runway.getId(), time);

        double duration = ac.getLandingStrategy().computeLandingDuration(ac, simulator.getWeather());
        double finish = time + duration;

        simulator.scheduleEvent(new RunwayReleaseEvent(finish, runway, ac, true));
        handleGateAssignment(ac, finish);
    }

    public void handleEmergencyLanding(Aircraft ac, double time) {
        attemptAssignRunwayForEmergency(ac, time);
    }

    private void attemptAssignRunwayForEmergency(Aircraft ac, double time) {
        Runway free = findAvailableRunway();

        if (free != null) {
            assignRunwayForLanding(ac, free, time, true);
        } else {
            simulator.logReal("Emergency " + ac.getId() + " added to emergency queue.");
            emergencyQueue.add(ac);
        }
    }

    public void handleTakeoffRequest(Aircraft ac, double time) {
        if (!canAssignTakeoff(ac)) return;

        if (takeoffBlocked) {
            if (!takeoffQueue.contains(ac)) takeoffQueue.add(ac);
            simulator.logReal("TAKEOFF BLOCKED by storm – " + ac.getId() + " must wait.");
            return;
        }

        if (time >= nightCurfewStart) {
            simulator.logReal("Night curfew active – cannot takeoff " + ac.getId());
            return;
        }

        Runway free = findAvailableRunway();

        if (free != null) {
            assignRunwayForTakeoff(ac, free, time);
        } else {
            if (!takeoffQueue.contains(ac)) takeoffQueue.add(ac);
        }
    }

    private void assignRunwayForTakeoff(Aircraft ac, Runway runway, double time) {
        runway.occupy();
        simulator.logReal(ac.getId() + " assigned to runway " + runway.getId() + " for takeoff");

        ac.takeoff();
        simulator.getStats().recordRunwayUse(runway.getId(), time);

        double duration = ac.getTakeoffStrategy().computeTakeoffDuration(ac, simulator.getWeather());
        double finish = time + duration;

        simulator.scheduleEvent(new RunwayReleaseEvent(finish, runway, ac, false));
        simulator.getStats().countTakeoff();
    }

    public void onRunwayReleased(Runway runway, double time, Aircraft ac, boolean isLanding) {
        runway.free();

        if (isLanding) {
            simulator.getStats().countSuccessfulLanding();
        }
        if (!emergencyQueue.isEmpty()) {
            assignRunwayForLanding(emergencyQueue.poll(), runway, time, true);
        
        } else if (!landingQueue.isEmpty()) {
            assignRunwayForLanding(landingQueue.poll(), runway, time, false);
        
        } else if (!takeoffQueue.isEmpty()) {
            if (takeoffBlocked) {
                return; 
            }
            
            Aircraft next = takeoffQueue.peek();
            if (canAssignTakeoff(next)) {
                takeoffQueue.poll();
                assignRunwayForTakeoff(next, runway, time);
            }
        }
    }

    private boolean canAssignTakeoff(Aircraft ac) {
        return ac != null && "READY_FOR_TAKEOFF".equals(ac.getStateName());
    }

    public void handleGateAssignment(Aircraft ac, double time) {
        Gate availableGate = null;
        for (Gate g : gateRegistry.getAll()) {
            if (g.isAvailable()) {
                availableGate = g;
                break;
            }
        }

        if (availableGate != null) {
            availableGate.occupy();
            simulator.logReal("Aircraft " + ac.getId() + " assigned to gate " + availableGate.getId());

            ac.gateAssigned();
            simulator.getStats().countServedAtGate();

            double turnaround = 10.0 + localRng.nextDouble() * 10.0;
            double gateFreeTime = time + turnaround;

            simulator.scheduleEvent(new GateReleaseEvent(gateFreeTime, availableGate, ac));

            double takeoffReqTime = gateFreeTime + 1.0;
            simulator.scheduleEvent(new TakeoffRequestEvent(takeoffReqTime, ac));

        } else {
            double retry = time + 1.0 + localRng.nextDouble() * 5.0;
            simulator.scheduleEvent(new GateSearchRetryEvent(retry, ac));
        }
    }

    public void releaseGate(Gate gate, double time) {
        gate.release();
        simulator.logReal("Gate " + gate.getId() + " released at " + time);
    }
}