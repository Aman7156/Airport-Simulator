public abstract class EventBase extends Event {

    public EventBase(double time) {
        super(time);
    }

    @Override
    public final void process(Simulator sim) {
        beforeProcess(sim);
        execute(sim);
        afterProcess(sim);
    }

    public void beforeProcess(Simulator sim) { }

    public void afterProcess(Simulator sim) { }

    public abstract void execute(Simulator sim);
}
