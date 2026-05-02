public class BusyState implements RunwayState {

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void occupy(Runway runway) {
    }

    @Override
    public void release(Runway runway) {
        runway.setState(new AvailableState());
    }

    @Override
    public String getName() {
        return "BUSY";
    }
}
