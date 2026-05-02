public abstract class Event implements Comparable<Event> {
    private final double time;

    public Event(double time) {
        this.time = time;
    }

    public double getTime() {
        return time;
    }

    public abstract void process(Simulator sim);

    @Override
    public int compareTo(Event o) {
        return Double.compare(time, o.time);
    }
}
