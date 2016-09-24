package hack.memphis.sousvide;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import hack.memphis.sousvide.databinding.ActivityControllerBinding;


/**
 * Created by isma on 9/23/16.
 */
public class ControllerActivity extends AppCompatActivity{
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
    }
}
