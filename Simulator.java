import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.PriorityQueue;
import java.util.Random;

public class Simulator {

    private double currentTime;

    private final TimeManager timeManager = new TimeManager(); 
        
    private final double endTime;
    
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private final Scheduler scheduler;

    private final Weather weather;
    private final WeatherStation weatherStation;

    private final Random rng;
    private final Statistics stats;

    private final double arrivalRate;
  
    private final double nightCurfewStart = 400.0;
    public double getNightCurfewStart(){ 
        return nightCurfewStart; 
    }


    private final DateTimeFormatter realtimeFmt = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public Simulator(int endTime, int runwaysCount, int gatesCount, double arrivalRate,
                     double emergencyProb, double vipProb, long seed) {

        this.endTime = endTime;
        this.arrivalRate = arrivalRate;
        
        rng = new Random(seed);
        weather = new Weather(rng);
        weatherStation = new WeatherStation(weather);
        stats = new Statistics();
        scheduler = new Scheduler(runwaysCount, gatesCount, this);

        weatherStation.addObserver(scheduler);
        stats.initializeRunways(scheduler.getRunways());

        for (Runway r : scheduler.getRunways()) {
            weatherStation.addObserver(r);
        }

        scheduler.setEmergProb(emergencyProb);
        scheduler.setVipProb(vipProb);
    }

    public TimeManager getTimeManager() { 
        return timeManager;
    }

private String lastPrintedTime = null;

public void logReal(String msg) {
    String now = LocalTime.now().format(realtimeFmt);

    if (lastPrintedTime == null || !now.equals(lastPrintedTime)) {
        System.out.println("[" + now + "] " + msg);
        lastPrintedTime = now;
    } else {
        System.out.printf("%-15s%s%n", "", msg);
    }
}

    public void scheduleEvent(Event e) {
        if (e.getTime() <= endTime) {
            eventQueue.add(e);
        }
    }

    public double getCurrentTime() { return currentTime; }
    public Scheduler getScheduler() { return scheduler; }
    public Weather getWeather() { return weather; }
    public WeatherStation getWeatherStation() { return weatherStation; }
    public Random getRng() { return rng; }
    public Statistics getStats() { return stats; }
    public double getEndTime() { return endTime; }

    public void initialize() {

        logReal("Simulation started");

        double t = currentTime + sampleExponential(arrivalRate);
        Aircraft ac = scheduler.createAircraft();

        weatherStation.addObserver(ac);

        scheduleEvent(EventFactory.createLandingEvent(t, ac));
        scheduleEvent(EventFactory.createWeatherEvent(30.0, weatherStation));

        ATCController atc = new ATCController();

        scheduleEvent(new EventBase(100.0) {
            
            public void execute(Simulator sim) {

                Aircraft emergencyAC = scheduler.createAircraft();
                emergencyAC.setEmergency(true);

                weatherStation.addObserver(emergencyAC);

                ATCCommand cmd =
                        new ForceLandingCommand(scheduler, emergencyAC, getTime());

                atc.issueCommand(cmd);
                logReal("ATC COMMAND: Force emergency landing");
            }
        });
    }

    public void run() {

        while (!eventQueue.isEmpty()) {

            Event e = eventQueue.poll();
            if (e.getTime() > endTime) break;

            currentTime = e.getTime();
            timeManager.advanceTo(currentTime);

            e.process(this);

            if (e instanceof LandingRequestEvent) {

                double nextTime = currentTime + sampleExponential(arrivalRate);

                if (nextTime <= endTime) {

                    Aircraft nextAC = scheduler.createAircraft();
                    weatherStation.addObserver(nextAC);

                    scheduleEvent(new LandingRequestEvent(nextTime, nextAC));
                }
            }
        }

        logReal("Simulation ended");
    }

    private double sampleExponential(double lambda) {
        if (lambda <= 0) return Double.POSITIVE_INFINITY;

        double u = rng.nextDouble();
        return -Math.log(1 - u) / lambda;
    }

    public void printReport() {
        System.out.println("\n--- Simulation Report ---");

        String realEnd = LocalTime.now().format(realtimeFmt);

        System.out.println(" Real time ended at: " + realEnd);
        System.out.println(" Simulation clock time: " + timeManager.format24Hour());

        stats.printSummary();
    }
}
