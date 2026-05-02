public class LandingState implements AircraftState {

    @Override
    public String getName() {
        return "LANDING";
    }

    @Override
    public void onLandingAssigned(Aircraft aircraft) { }

    @Override
    public void onGateAssigned(Aircraft aircraft) {
        aircraft.setState(new AtGateState());
    }

    @Override
    public void onReadyForTakeoff(Aircraft aircraft) { }

    @Override
    public void onTakeoff(Aircraft aircraft) { }
}
