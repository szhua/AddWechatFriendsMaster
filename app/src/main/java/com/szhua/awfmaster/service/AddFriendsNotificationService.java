package com.szhua.awfmaster.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.szhua.awfmaster.inter.IStatusBarNotification;
import com.szhua.awfmaster.service.AddWechatFriendsService;

/**
 * Created by szhua on 2016/12/15.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AddFriendsNotificationService extends NotificationListenerService {

    private static AddWechatFriendsService service ;


    @Override
    public void onCreate() {
        super.onCreate();




    }

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);


        sendBroadcast(new Intent());


        if(AddFriendsNotificationService.isRunning()){
            AddWechatFriendsService.handleNotification(new IStatusBarNotification() {
                @Override
                public String getPackageName() {
                    return sbn.getPackageName();
                }
                @Override
                public Notification getNotification() {
                    return sbn.getNotification();
                }
            });
        }
    }



    public  static  boolean isRunning(){
        if(service!=null){
            return  true ;
        }
        return  false ;
    }


}
