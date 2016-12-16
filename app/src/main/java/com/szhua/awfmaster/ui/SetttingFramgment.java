package com.szhua.awfmaster.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;

import com.szhua.awfmaster.Config;
import com.szhua.awfmaster.R;
import com.szhua.awfmaster.base.BaseSettingFragment;
import com.szhua.awfmaster.service.AddFriendsNotificationService;
import com.szhua.awfmaster.service.AddWechatFriendsService;

/**
 * AddWechatFriendsMaster
 * Create   2016/12/15 16:11;
 * https://github.com/szhua
 *
 * @author sz.hua
 */
public class SetttingFramgment extends BaseSettingFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_setting);

        //添加好友服务的开关；


        Preference wechatPref = findPreference(Config.KEY_ENABLE_WECHAT);
        wechatPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue && !AddWechatFriendsService.isRunning()) {
                    ((HomeActivity)getActivity()).checkServiceIsAvailable();
                }else{

                }
                return true;
            }
        });

        //进行添加好友
        Preference startPref =findPreference(Config.KEY_START_WECHAT);
        startPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //启动微信界面；
                Intent intent =new Intent() ;
                ComponentName componentName =new ComponentName(Config.WECHAT_PACKAGE_NANE,Config.WECHAT_LANCHER_UI_CLASS_NAME);
                intent.setComponent(componentName);
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER) ;
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            }
        });

    }
}
