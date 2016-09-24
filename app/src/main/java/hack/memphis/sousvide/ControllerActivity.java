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
    private ActivityControllerBinding mBinding;

    private char mUnits = 'C';


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_controller);

        //Set the toolbar
        setSupportActionBar(mBinding.controllerToolbar);

        //Setup the pickers
        mBinding.controllerTemperaturePicker.setMinValue(30);
        mBinding.controllerTemperaturePicker.setMaxValue(100);
        mBinding.controllerTemperaturePicker.setValue(50);
        mBinding.controllerTemperaturePicker.setWrapSelectorWheel(false);

        mBinding.controllerTimePicker.setMinValue(15);
        mBinding.controllerTimePicker.setMaxValue(360);
        mBinding.controllerTimePicker.setValue(90);
        mBinding.controllerTimePicker.setWrapSelectorWheel(false);

        mBinding.controllerTemperature.setOnClickListener(this);
        mBinding.controllerTemperatureUnits.setOnClickListener(this);
        mBinding.controllerSwitch.setOnClickListener(this);

        HttpRequest.init(getApplicationContext());
        HttpRequest.get(this, "https://sousvide.lyth.io/api/configuration/");
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.controller_temperature:
            case R.id.controller_temperature_units:
                int currentValue = mBinding.controllerTemperaturePicker.getValue();
                if (mUnits == 'C'){
                    mBinding.controllerTemperatureUnits.setText("(ºF, switch to C)");
                    int newValue = Math.min(210, Math.max(85, CtoF(currentValue)));
                    mBinding.controllerTemperaturePicker.setMinValue(85);
                    mBinding.controllerTemperaturePicker.setMaxValue(210);
                    mBinding.controllerTemperaturePicker.setValue(newValue);
                    mUnits = 'F';
                }
                else if (mUnits == 'F'){
                    mBinding.controllerTemperatureUnits.setText("(ºC, switch to F)");
                    int newValue = Math.min(100, Math.max(30, FtoC(currentValue)));
                    mBinding.controllerTemperaturePicker.setMinValue(30);
                    mBinding.controllerTemperaturePicker.setMaxValue(100);
                    mBinding.controllerTemperaturePicker.setValue(newValue);
                    mUnits = 'C';
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
