package com.szhua.awfmaster.job;

import android.view.accessibility.AccessibilityEvent;

import com.szhua.awfmaster.inter.IStatusBarNotification;
import com.szhua.awfmaster.service.AddWechatFriendsService;
import com.szhua.awfmaster.service.BaseAccessibilityService;

/**
 * AddWechatFriendsMaster
 * Create   2016/12/15 15:57;
 * https://github.com/szhua
 *
 * @author sz.hua
 */
public interface AccessbilityJob {

    String getPackageName() ;
    void onCreateJob(BaseAccessibilityService service) ;
    void onStopJob() ;
    void onReceiveJob(AccessibilityEvent event);
    boolean isEnable();
    void handleNotificationPosted(IStatusBarNotification iStatusBarNotification);


}
