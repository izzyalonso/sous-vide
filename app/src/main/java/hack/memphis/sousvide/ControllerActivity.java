package hack.memphis.sousvide;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
    private ActivityControllerBinding binding;

    private char units = 'C';


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

        HttpRequest.init(getApplicationContext());
        HttpRequest.get(this, "https://sousvide.lyth.io/api/configuration/");
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.controller_temperature:
            case R.id.controller_temperature_units:
                int currentValue = binding.controllerTemperaturePicker.getValue();
                if (units == 'C'){
                    binding.controllerTemperatureUnits.setText("(ºF, switch to C)");
                    int newValue = Math.min(210, Math.max(85, CtoF(currentValue)));
                    binding.controllerTemperaturePicker.setMinValue(85);
                    binding.controllerTemperaturePicker.setMaxValue(210);
                    binding.controllerTemperaturePicker.setValue(newValue);
                    units = 'F';
                }
                else if (units == 'F'){
                    binding.controllerTemperatureUnits.setText("(ºC, switch to F)");
                    int newValue = Math.min(100, Math.max(30, FtoC(currentValue)));
                    binding.controllerTemperaturePicker.setMinValue(30);
                    binding.controllerTemperaturePicker.setMaxValue(100);
                    binding.controllerTemperaturePicker.setValue(newValue);
                    units = 'C';
                }
                break;


        }
    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        Log.d("ControllerRequest", result);
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){

    }

    private int CtoF(int value){
        return (int)(value*1.8)+32;
    }

    private int FtoC(int value){
        return (int)((value-32)/1.8);
    }
}
