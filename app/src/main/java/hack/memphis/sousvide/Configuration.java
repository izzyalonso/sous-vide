package hack.memphis.sousvide;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Configuration model class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Configuration{
    private boolean running;
    private double temperature;
    private long startTime;
    private int duration;


    /**
     * Constructor.
     *
     * @param temperature the temperature.
     * @param duration the duration.
     */
    public Configuration(double temperature, int duration){
        this.temperature = temperature;
        this.duration = duration;
    }

    public boolean isRunning(){
        return running;
    }

    public boolean isDone(){
        return startTime+duration*60*1000 < System.currentTimeMillis();
    }

    public double getTemperature(){
        return temperature;
    }

    public long getStartTime(){
        return startTime;
    }

    public int getDuration(){
        return duration;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try{
            json.put("temperature", temperature);
            json.put("duration", duration);
        }
        catch (JSONException jx){
            jx.printStackTrace();
        }
        return json;
    }

    @Override
    public String toString(){
        return (running ? "Running: " + temperature + " ºC, " + duration + " minutes" : "Not running.");
    }

    public static Configuration fromJson(String configuration){
        return new Gson().fromJson(configuration, Configuration.class);
    }
}
