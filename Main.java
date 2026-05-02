import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
       
        Properties config = new Properties();
        try (InputStream in = new FileInputStream("config.txt")) {
            config.load(in);
            System.out.println("Loaded configuration from config.txt");
        } catch (Exception e) {
            System.out.println("config.txt not found");
        }

        int runways = Integer.parseInt(config.getProperty("runways", "2"));
        int gates = Integer.parseInt(config.getProperty("gates", "4"));
        int simulationTime = Integer.parseInt(config.getProperty("simulationTime", "1000")); 
        double arrivalRate = Double.parseDouble(config.getProperty("arrivalRate", "0.05")); 
        double emergProb = Double.parseDouble(config.getProperty("emergencyProb", "0.02"));
        double vipProb = Double.parseDouble(config.getProperty("vipProb", "0.03"));
        long seed = Long.parseLong(config.getProperty("seed", String.valueOf(System.currentTimeMillis())));

 
        Simulator sim = new Simulator(simulationTime, runways, gates, arrivalRate, emergProb, vipProb, seed);
        sim.initialize();  
        sim.run();         
        sim.printReport(); 
    }
}
