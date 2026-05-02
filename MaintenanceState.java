public class MaintenanceState implements RunwayState {

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void occupy(Runway runway) {
        
    }

    @Override
    public void release(Runway runway) {
    }
    @Override
    public String getName() {
        return "MAINTENANCE";
    }
}
