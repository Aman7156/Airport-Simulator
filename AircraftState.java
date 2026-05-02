public interface AircraftState {

    public String getName();

    public void onLandingAssigned(Aircraft aircraft);

    public void onGateAssigned(Aircraft aircraft);
    public void onReadyForTakeoff(Aircraft aircraft);
    public void onTakeoff(Aircraft aircraft);
}
