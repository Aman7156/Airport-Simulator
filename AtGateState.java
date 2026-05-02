public class AtGateState implements AircraftState {

    @Override
    public String getName() {
        return "AT_GATE";
    }
    @Override
    public void onLandingAssigned(Aircraft aircraft) { }

    @Override
    public void onGateAssigned(Aircraft aircraft) { }

    @Override
    public void onReadyForTakeoff(Aircraft aircraft) {
        aircraft.setState(new ReadyForTakeoffState());
    }

    @Override
    public void onTakeoff(Aircraft aircraft) { }
}
