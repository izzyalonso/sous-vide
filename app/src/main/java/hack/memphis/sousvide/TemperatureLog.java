package hack.memphis.sousvide;

/**
 * Log model class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class TemperatureLog{
    private static int count = 0;

    public static void reset(){
        count = 0;
    }

    private double temperature;
    private long timestamp;


    public double getTemperature(){
        return temperature;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public boolean display(long start){
        return getMinuteInternal(start) >= count;
    }

    private int getMinuteInternal(long start){
        int elapsed = (int)(timestamp-start);
        return elapsed/(60*1000);
    }

    public int getMinute(long start){
        count++;
        return getMinuteInternal(start);
    }
}
