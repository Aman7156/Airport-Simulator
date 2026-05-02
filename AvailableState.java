public class AvailableState implements RunwayState {

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void occupy(Runway runway) {
        runway.setState(new BusyState());
    }

    @Override
    public void release(Runway runway) {
        
    }

    @Override
    public String getName() {
        return "AVAILABLE";
    }
}
