import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

   
    private int arrivalRequests; //0
    private int successfulLandings; //0
    private int refusedLandings; //0
    private int takeoffs; //0
    private int emergencies; //0
    private int servedAtGate; //0

   
    private final Map<String, Double> runwayUsageStart = new HashMap<>();
    private final Map<String, Double> runwayUsageTotal = new HashMap<>();
    

   
    public void initializeRunways(List<Runway> runways) {
        for (Runway r : runways) {
            runwayUsageTotal.put(r.getId(), 0.0);
        }
    }

    public void countArrivalRequest() {
        arrivalRequests++;
    }

    public void countSuccessfulLanding() {
        successfulLandings++;
    }

    public void countRefusedLanding() {
        refusedLandings++;
    }

    public void countTakeoff() {
        takeoffs++;
    }

    public void countEmergency() {
        emergencies++;
    }

    public void countServedAtGate() {
        servedAtGate++;
    }

    public void recordRunwayUse(String runwayId, double time) {
        runwayUsageStart.put(runwayId, time);
    }

    public void recordRunwayFree(String runwayId, double time) {

        if (!runwayUsageStart.containsKey(runwayId)) return;

        double start = runwayUsageStart.remove(runwayId);
        double duration = Math.max(0.0, time - start);

        double updatedTotal = runwayUsageTotal.getOrDefault(runwayId, 0.0) + duration;
        runwayUsageTotal.put(runwayId, updatedTotal);
    }

    private String toHHMM(double minutes) {
        int m = (int)minutes;
        return String.format("%02d:%02d", (m / 60), (m % 60));
    }

    public void printSummary() {
        System.out.println("\n--- Simulation Statistics ---");

        System.out.printf("Arrival requests: %d\n", arrivalRequests);
        System.out.printf("Landed successfully: %d\n", successfulLandings);
        System.out.printf("Refused landings: %d\n", refusedLandings);

        System.out.printf("Total takeoffs: %d\n", takeoffs);
        System.out.printf("Emergencies handled: %d\n", emergencies);
        System.out.printf("Served at gates: %d\n", servedAtGate);

        System.out.println("\nRunway utilization (HH:MM):");
        for (Map.Entry<String, Double> e : runwayUsageTotal.entrySet()) {
            System.out.printf("  Runway %s -> %s\n",
                    e.getKey(), toHHMM(e.getValue()));
        }
    }
}
