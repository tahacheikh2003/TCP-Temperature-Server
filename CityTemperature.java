public class CityTemperature {// Latest temperature recorded for the city
    private double temperature;
    private long timestamp;

    public CityTemperature(double temperature) { // Constructor: initializes the temperature and records the current time
        this.temperature = temperature;
        this.timestamp = System.currentTimeMillis();
    }

    public double getTemperature() {
        return temperature;
    }

    public long getTimestamp() {// Returns the timestamp of the last update
        return timestamp;
    }
}