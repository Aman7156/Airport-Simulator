import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class Weather {

    private final Random rng;

    private String condition = "Clear";
    private double landingDelayFactor; //0.0
    private double takeoffDelayFactor; //0.0

    public Weather(Random rng) {
        this.rng = rng;
    }


    public void randomize() {
        double p = rng.nextDouble();

        if (p < 0.7) {
            condition = "Clear";
        }
        else if (p < 0.85) {
            condition = "Fog";
            landingDelayFactor = 2.5;
            takeoffDelayFactor = 1.5;
        }
        else if (p < 0.95) {
            condition = "Storm";
            landingDelayFactor = 5.0;
            takeoffDelayFactor = 3.0;
        }
        else {
            condition = "Severe";
            landingDelayFactor = 8.0;
            takeoffDelayFactor = 5.0;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        System.out.printf("[%s] Weather changed: %s (landing delay=%.2f, takeoff delay=%.2f)%n",
                LocalTime.now().format(formatter),
                condition,
                landingDelayFactor,
                takeoffDelayFactor);
    }

    public boolean isStorm() {
        return condition.equals("Storm") || condition.equals("Severe");
    }

    public String getCondition() { return condition; }
    public double getLandingDelayFactor() { return landingDelayFactor; }
    public double getTakeoffDelayFactor() { return takeoffDelayFactor; }

}
