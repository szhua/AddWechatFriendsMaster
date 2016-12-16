package com.szhua.awfmaster.base;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.szhua.awfmaster.Config;

/**
 * AddWechatFriendsMaster
 * Create   2016/12/15 14:49;
 * https://github.com/szhua
 *
 * @author sz.hua
 */
public class AddFriendsMasterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(Config.TAG) ;
    }
}
