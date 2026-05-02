public interface RunwayState {

    public boolean isAvailable();
    public void occupy(Runway runway);
    public void release(Runway runway);
    public String getName();
}
