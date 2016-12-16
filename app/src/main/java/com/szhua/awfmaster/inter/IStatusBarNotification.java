package com.szhua.awfmaster.inter;

import android.app.Notification;

/**
 * Created by szhua on 2016/12/15.
 */
public interface IStatusBarNotification {

    String getPackageName () ;
    Notification getNotification() ;
}
