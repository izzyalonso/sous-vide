package hack.memphis.sousvide;

/**
 * Log model class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class TemperatureLog{
    private double temperature;
    private long timestamp;


    public double getTemperature(){
        return temperature;
    }

    public long getTimestamp(){
        return timestamp;
    }
}
