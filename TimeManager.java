public class TimeManager {

    private double currentTime; //0.0 

    public double getTime() {
        return currentTime;
    }

    public void advanceTo(double newTime) {
        if (newTime >= currentTime) {
            currentTime = newTime;
        }
    }

    public void reset() {
        currentTime = 0.0;
    }

    
    public String format24Hour() {
        int totalMinutes = (int) currentTime;

        int hours = (totalMinutes / 60) % 24;
        int minutes = totalMinutes % 60;

        return String.format("%02d:%02d", hours, minutes);
    }

    public String formatRaw() {
        return String.format("%.2f", currentTime);
    }
}
