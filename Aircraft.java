public class Aircraft implements WeatherObserver,Comparable<Aircraft> {
    private final String id;
    private AircraftType type;
    private boolean emergency; //false
    private AircraftState state;
    private LandingStrategy landingStrategy;
    private TakeoffStrategy takeoffStrategy;

    public Aircraft(String id, AircraftType type) {
        this.id = id;
        this.type = type;
        state = new AirborneState(); 
    }
   
        @Override
        public int compareTo(Aircraft other) {
            if (isEmergency() && !other.isEmergency()) {
                return -1; 
            }
     
            if (!isEmergency() && other.isEmergency()) {
                return 1;
            }
            return id.compareTo(other.id);
        }
    
    public String getId() { return id; }
    public String getType() { return type.name(); }
    public AircraftType getAircraftType() { return type; }

    public void setState(AircraftState newState) {
        state = newState;
        System.out.println(" Aircraft " + id + " state  " + state.getName());
    }

    public String getStateName() {
        return state.getName();
    }

    public void landingAssigned() {
        state.onLandingAssigned(this);
    }

    public void gateAssigned() {
        state.onGateAssigned(this);
    }

    public void readyForTakeoff() {
        state.onReadyForTakeoff(this);
    }

    public void takeoff() {
        state.onTakeoff(this);
    }

    public void setLandingStrategy(LandingStrategy strategy) {
        landingStrategy = strategy;
    }

    public LandingStrategy getLandingStrategy() {
        return landingStrategy;
    }

    public void setTakeoffStrategy(TakeoffStrategy strategy) {
        takeoffStrategy = strategy;
    }

    public TakeoffStrategy getTakeoffStrategy() {
        return takeoffStrategy;
    }


    public boolean isEmergency() {
         return emergency; 
        }

    public void setEmergency(boolean isEmergency) {
         emergency = isEmergency; 
         if (isEmergency) {
            type = AircraftType.EMERGENCY;
            System.out.println("!!! ALERT: Aircraft " + id + " declared " + type +"!!!"); 
        }
        }

    @Override
    public void onWeatherUpdate(Weather weather) {
        
    }
    public enum AircraftType {
        PASSENGER,
        CARGO,
        VIP,
        EMERGENCY
    }
}
