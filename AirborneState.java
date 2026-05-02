public class AirborneState implements AircraftState {


    @Override
    public String getName() {
        return "AIRBORNE";
    }

    @Override
    public void onLandingAssigned(Aircraft aircraft) {
        aircraft.setState(new LandingState());
    }

    @Override
    public void onGateAssigned(Aircraft aircraft) { }

    @Override
    public void onReadyForTakeoff(Aircraft aircraft) { }

    @Override
    public void onTakeoff(Aircraft aircraft) { }
}