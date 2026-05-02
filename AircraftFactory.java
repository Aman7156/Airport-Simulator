import java.util.Random;

public class AircraftFactory {

    private final Random rng;
    private double emergencyProb;
    private double vipProb;

    private int aircraftCounter;  //0

    public AircraftFactory(Random rng, double emergencyProb, double vipProb) {
        this.rng = rng;
        this.emergencyProb = emergencyProb;
        this.vipProb = vipProb;
    }

   
    public void setEmergencyProb(double p) {
        emergencyProb = p;
    }

    public void setVipProb(double p) {
        vipProb = p;
    }


    public Aircraft createAircraft() {

        aircraftCounter++;
        double r = rng.nextDouble();

        Aircraft.AircraftType type;

        if (r < emergencyProb) {
            type = Aircraft.AircraftType.EMERGENCY;
        }
        else if (r < emergencyProb + vipProb) {
            type = Aircraft.AircraftType.VIP;
        }
        else if (r < 0.6) {
            type = Aircraft.AircraftType.PASSENGER;
        }
        else {
            type = Aircraft.AircraftType.CARGO;
        }

        Aircraft ac = new Aircraft("AC" + aircraftCounter, type);

        if (type == Aircraft.AircraftType.PASSENGER) {
            ac.setLandingStrategy(new PassengerLandingStrategy());
            ac.setTakeoffStrategy(new PassengerTakeoffStrategy());
        
        } else if (type == Aircraft.AircraftType.CARGO) {
            ac.setLandingStrategy(new CargoLandingStrategy());
            ac.setTakeoffStrategy(new CargoTakeoffStrategy());
        
        } else if (type == Aircraft.AircraftType.VIP) {
            ac.setLandingStrategy(new VipLandingStrategy());
            ac.setTakeoffStrategy(new VipTakeoffStrategy());
        
        } else if (type == Aircraft.AircraftType.EMERGENCY) {
            ac.setEmergency(true);
            ac.setLandingStrategy(new EmergencyLandingStrategy());
            ac.setTakeoffStrategy(new EmergencyTakeoffStrategy());
        }

        return ac;
    }
}
