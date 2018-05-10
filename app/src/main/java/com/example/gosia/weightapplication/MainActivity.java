package com.example.gosia.weightapplication;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gosia.weightapplication.databinding.ActivityMainBinding;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;




public class MainActivity extends AppCompatActivity {

    public final String LOGGER = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction()
                    .add(R.id.fragmentContainer, new OpeningFragment())
                    .commit();
        }


        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setVariable(this);

        Logger.addLogAdapter(new AndroidLogAdapter());


        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else super.onBackPressed();
    }
}
