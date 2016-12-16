package com.szhua.awfmaster.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.szhua.awfmaster.Config;
import com.szhua.awfmaster.R;
import com.szhua.awfmaster.base.BaseActivity;
import com.szhua.awfmaster.base.BaseSettingActivity;
import com.szhua.awfmaster.inter.AutoServiceRunningCallBack;
import com.szhua.awfmaster.service.AddWechatFriendsService;

public class HomeActivity extends BaseSettingActivity {
    protected AlertDialog mTipsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*register receiver*/
        IntentFilter filter =new IntentFilter() ;
        filter.addAction(Config.ACTION_ADDFRIENDS_SERVICE_CONNECT);
        filter.addAction(Config.ACTION_ADDFRIENDS_NOTIFICATION_SERVICE_CONNECT);
        registerReceiver(addWechatFriendsReceiver,filter) ;


        setTitle(R.string.app_name);
    }

    @Override
    public Fragment getSettingFragment() {
        return new SetttingFramgment();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkServiceIsAvailable();
    }

    private BroadcastReceiver addWechatFriendsReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           String action =intent.getAction() ;
            switch (action){
                case Config.ACTION_ADDFRIENDS_SERVICE_CONNECT:
                if (mTipsDialog!=null){
                    mTipsDialog.cancel();
                }
                break;
                case Config.ACTION_ADDFRIENDS_NOTIFICATION_SERVICE_CONNECT:
                    break;
            }
        }
    } ;



    /** 显示未开启辅助服务的对话框*/
    protected void showOpenAccessibilityServiceDialog() {
        if(mTipsDialog != null && mTipsDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("需要开启辅助服务正常使用");
        builder.setCancelable(false) ;
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("去打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAccessibilityServiceSettings();
            }
        });
        mTipsDialog = builder.show();
    }

    /** 打开辅助服务的设置*/
    protected void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, getString(R.string.get_permission_for_accessibility), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
      判断service是否可用；
      */
    public  void checkServiceIsAvailable(){
        if(AddWechatFriendsService.isRunning()) {
            if(mTipsDialog != null) {
                mTipsDialog.dismiss();
            }
        } else {
            showOpenAccessibilityServiceDialog();
        }
    }

}
