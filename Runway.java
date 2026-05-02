public class Runway implements WeatherObserver {

    private final String id;
    private RunwayState state;

    public Runway(String id) {
        this.id = id;
        state = new AvailableState(); 
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return state.isAvailable();
    }

    public void occupy() {
        state.occupy(this);
    }

    public void free() {
        state.release(this);
    }

    void setState(RunwayState newState) {
        state = newState;
    }

    @Override
    public void onWeatherUpdate(Weather weather) {

        if (weather.isStorm() && !(state instanceof MaintenanceState)) {
            System.out.println(" Runway " + id + " entering MAINTENANCE due to weather");
            setState(new MaintenanceState());
        }

        if (!weather.isStorm() && state instanceof MaintenanceState) {
            System.out.println(" Runway " + id + " exiting MAINTENANCE");
            setState(new AvailableState());
        }
    }
}
