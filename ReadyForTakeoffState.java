public class ReadyForTakeoffState implements AircraftState {

    @Override
    public String getName() {
        return "READY_FOR_TAKEOFF";
    }

    @Override
    public void onLandingAssigned(Aircraft aircraft) { }

    @Override
    public void onGateAssigned(Aircraft aircraft) { }

    @Override
    public void onReadyForTakeoff(Aircraft aircraft) { }

    @Override
    public void onTakeoff(Aircraft aircraft) {
        aircraft.setState(new AirborneState());
    }
}