package hack.memphis.sousvide;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import es.sandwatch.httprequests.HttpRequest;
import es.sandwatch.httprequests.HttpRequestError;

import hack.memphis.sousvide.databinding.ActivityControllerBinding;


/**
 * Main controller activity.
 *
 * @author Ismael Alonso
 * @version 1.0.0
 */
public class ControllerActivity extends AppCompatActivity implements HttpRequest.RequestCallback, View.OnClickListener{
    private static final String SCALE_KEY = "scale";


    private ActivityControllerBinding binding;

    private String scale;
    private Configuration configuration;

    private int getConfigRC;
    private int postConfigRC;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_controller);

        //Set the toolbar
        setSupportActionBar(binding.controllerToolbar);

        //Setup the pickers
        binding.controllerTemperaturePicker.setMinValue(30);
        binding.controllerTemperaturePicker.setMaxValue(100);
        binding.controllerTemperaturePicker.setValue(50);
        binding.controllerTemperaturePicker.setWrapSelectorWheel(false);

        binding.controllerMinutePicker.setMinValue(0);
        binding.controllerMinutePicker.setMaxValue(60);
        binding.controllerMinutePicker.setValue(30);
        binding.controllerMinutePicker.setWrapSelectorWheel(false);

        binding.controllerHourPicker.setMinValue(0);
        binding.controllerHourPicker.setMaxValue(60);
        binding.controllerHourPicker.setValue(1);
        binding.controllerHourPicker.setWrapSelectorWheel(false);

        binding.controllerTemperature.setOnClickListener(this);
        binding.controllerTemperatureUnits.setOnClickListener(this);
        binding.controllerSwitch.setOnClickListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        scale = prefs.getString(SCALE_KEY, "C");
        Log.d("Controller", scale);

        HttpRequest.init(getApplicationContext());
        getConfigRC = HttpRequest.get(this, "https://sousvide.lyth.io/api/configuration/");
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.controller_temperature:
            case R.id.controller_temperature_units:
                int currentValue = binding.controllerTemperaturePicker.getValue();
                if (scale.equalsIgnoreCase("C")){
                    binding.controllerTemperatureUnits.setText("(ºF, switch to C)");
                    int newValue = Math.min(210, Math.max(85, CtoF(currentValue)));
                    binding.controllerTemperaturePicker.setMinValue(85);
                    binding.controllerTemperaturePicker.setMaxValue(210);
                    binding.controllerTemperaturePicker.setValue(newValue);
                    scale = "F";
                }
                else if (scale.equalsIgnoreCase("F")){
                    binding.controllerTemperatureUnits.setText("(ºC, switch to F)");
                    int newValue = Math.min(100, Math.max(30, FtoC(currentValue)));
                    binding.controllerTemperaturePicker.setMinValue(30);
                    binding.controllerTemperaturePicker.setMaxValue(100);
                    binding.controllerTemperaturePicker.setValue(newValue);
                    scale = "C";
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SCALE_KEY, scale);
                editor.apply();
                break;

            case R.id.controller_switch:
                if (configuration.isRunning()){
                    HttpRequest.delete(this, "https://sousvide.lyth.io/api/configuration/");

                    binding.controllerTemperaturePicker.setEnabled(true);
                    binding.controllerHourPicker.setEnabled(true);
                    binding.controllerMinutePicker.setEnabled(true);

                    binding.controllerSwitch.setText("Start");
                }
                else{
                    int temperature = binding.controllerTemperaturePicker.getValue();
                    if (scale.equalsIgnoreCase("F")){
                        temperature = FtoC(temperature);
                    }
                    int duration = binding.controllerMinutePicker.getValue();
                    duration += binding.controllerHourPicker.getValue()*60;
                    Configuration configuration = new Configuration(temperature, duration);
                    String url = "https://sousvide.lyth.io/api/configuration/";
                    postConfigRC = HttpRequest.post(this, url, configuration.toJson());

                    binding.controllerTemperaturePicker.setEnabled(false);
                    binding.controllerHourPicker.setEnabled(false);
                    binding.controllerMinutePicker.setEnabled(false);
                    binding.controllerSwitch.setEnabled(false);
                    binding.controllerPostProgress.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        Log.d("ControllerRequest", result);
        if (requestCode == getConfigRC){
            setConfiguration(Configuration.fromJson(result));
        }
        else if (requestCode == postConfigRC){
            configuration = Configuration.fromJson(result);

            binding.controllerSwitch.setEnabled(true);
            binding.controllerSwitch.setText("Stop");
            binding.controllerPostProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){

    }

    private void setConfiguration(Configuration configuration){
        this.configuration = configuration;
        if (configuration.isRunning()){
            binding.controllerTemperaturePicker.setValue((int)configuration.getTemperature());
            binding.controllerTemperaturePicker.setEnabled(false);
            binding.controllerHourPicker.setValue(configuration.getDuration() / 60);
            binding.controllerHourPicker.setEnabled(false);
            binding.controllerMinutePicker.setValue(configuration.getDuration() % 60);
            binding.controllerMinutePicker.setEnabled(false);

            binding.controllerSwitch.setText("Stop");
        }

        if (scale.equalsIgnoreCase("F")){
            binding.controllerTemperatureUnits.setText("(ºF, switch to C)");
            int temperature = binding.controllerTemperaturePicker.getValue();
            int newValue = Math.min(210, Math.max(85, CtoF(temperature)));
            binding.controllerTemperaturePicker.setMinValue(85);
            binding.controllerTemperaturePicker.setMaxValue(210);
            binding.controllerTemperaturePicker.setValue(newValue);
        }

        binding.controllerLoadProgress.setVisibility(View.GONE);
        binding.controllerContentContainer.setVisibility(View.VISIBLE);
    }

    private int CtoF(int value){
        return (int)(value*1.8)+32;
    }

    private int FtoC(int value){
        return (int)((value-32)/1.8);
    }
}
