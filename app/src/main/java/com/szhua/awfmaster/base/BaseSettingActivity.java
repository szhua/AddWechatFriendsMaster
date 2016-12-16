package com.szhua.awfmaster.base;


import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.szhua.awfmaster.R;

public abstract class BaseSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setting);
        getFragmentManager().beginTransaction().add(R.id.container,getSettingFragment(),"settingFragment").commitAllowingStateLoss() ;

    }


    public abstract Fragment getSettingFragment () ;



}
