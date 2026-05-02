import java.util.ArrayList;
import java.util.List;

public class WeatherStation {

    private final Weather weather;  
    private final List<WeatherObserver> observers = new ArrayList<>();

    public WeatherStation(Weather weather) {
        this.weather = weather;
    }

    public void addObserver(WeatherObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(WeatherObserver obs) {
        observers.remove(obs);
    }

    public Weather getWeather() {
        return weather;
    }

    public void updateWeather() {
        weather.randomize();

        for (WeatherObserver obs : observers) {
            obs.onWeatherUpdate(weather);
        }
    }
}
