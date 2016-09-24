package hack.memphis.sousvide;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Configuration model class.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class Configuration{
    private double temperature;
    private String scale;
    private long startTime;
    private int duration;


    /**
     * Constructor.
     *
     * @param temperature the temperature.
     * @param scale the scale.
     * @param duration the duration.
     */
    public Configuration(double temperature, char scale, int duration){
        this.temperature = temperature;
        this.scale = scale+"";
        this.duration = duration;
    }

    public double getTemperature(){
        return temperature;
    }

    public char getScale(){
        if (scale.equalsIgnoreCase("f")){
            return 'f';
        }
        return 'c';
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
            json.put("scale", scale);
            json.put("duration", duration);
        }
        catch (JSONException jx){
            jx.printStackTrace();
        }
        return json;
    }
}
