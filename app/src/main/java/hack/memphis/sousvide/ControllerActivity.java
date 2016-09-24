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
 * Created by isma on 9/23/16.
 */
public class ControllerActivity extends AppCompatActivity implements HttpRequest.RequestCallback, View.OnClickListener{
    private ActivityControllerBinding mBinding;


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

        mBinding.controllerSwitch.setOnClickListener(this);

        HttpRequest.init(getApplicationContext());
        HttpRequest.get(this, "https://sousvide.lyth.io/api/configuration/");
    }

    @Override
    public void onClick(View view){

    }

    @Override
    public void onRequestComplete(int requestCode, String result){
        Log.d("ControllerRequest", result);
    }

    @Override
    public void onRequestFailed(int requestCode, HttpRequestError error){

    }
}
