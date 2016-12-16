package com.szhua.awfmaster.service;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import com.orhanobut.logger.Logger;
import com.szhua.awfmaster.Config;
import com.szhua.awfmaster.R;
import com.szhua.awfmaster.inter.IStatusBarNotification;
import com.szhua.awfmaster.job.AccessbilityJob;
import com.szhua.awfmaster.job.AddWechatFriendsJob;
import com.szhua.awfmaster.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by szhua on 2016/12/15.
 */
public class AddWechatFriendsService extends BaseAccessibilityService {
    private static  AddWechatFriendsService service ;

    /*jobs for accessbilityJob*/
    private List<AccessbilityJob> accessbilityJobs ;
    /*
    * k: packageName
    * v: AccessbilityJob
    * the cache for jobs
    * */
    private HashMap<String,AccessbilityJob> accessbilityJobHashMap ;

    private Class  [] allJobs =new Class[]{AddWechatFriendsJob.class} ;


    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 将添加微信好友的插件添加在此
         */
        accessbilityJobs =new ArrayList<>();
        accessbilityJobHashMap =new HashMap<>() ;
        for (Class allJob : allJobs) {

            try {
                Object  object =allJob.newInstance() ;
                if(object instanceof  AccessbilityJob){
                    AccessbilityJob job = (AccessbilityJob) object;
                    //set service into job ;BaseActivity
                    job.onCreateJob(this);
                    accessbilityJobs.add(job);
                    accessbilityJobHashMap.put(job.getPackageName(),job);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
         service =this;

         /*notify service is connected!*/
         sendBroadcast(new Intent(Config.ACTION_ADDFRIENDS_SERVICE_CONNECT));

         ToastUtil.showToast(getApplicationContext(),getString(R.string.service_connected),false);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        //开始工作；
        String pcgName =String.valueOf(accessibilityEvent.getPackageName());
        if(accessbilityJobs!=null&&!accessbilityJobs.isEmpty()){
            for (AccessbilityJob accessbilityJob : accessbilityJobs) {
                if(pcgName.equals(accessbilityJob.getPackageName())&&accessbilityJob.isEnable()){
                    accessbilityJob.onReceiveJob(accessibilityEvent);
                }
            }

        }
    }
    @Override
    public void onInterrupt() {
    ToastUtil.showToast(getApplicationContext(),"断开",false);
    }
    public Config getConfig(){
        if(service!=null)
            return  Config.getConfig(service) ;
        return  Config.getConfig(this);
    };
    /*判断当前服务是否正在运行*/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean isRunning() {
        if(service == null) {
            return false;
        }
        /*是否包含此服务*/
        AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = service.getServiceInfo();
        if(info == null) {
            return false;
        }
        /*正在运行的服务时候包含注册的服务；*/
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();
        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if(i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if(!isConnect) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessbilityJobs .clear();
        accessbilityJobHashMap.clear();
        accessbilityJobs =null ;
        accessbilityJobs =null ;
    }

    /**
     * 处理通知栏事件 ;
     * @param iStatusBarNotification
     */
    public static  void handleNotification(IStatusBarNotification iStatusBarNotification){
        if(iStatusBarNotification==null)return;

        if(service==null||service.accessbilityJobHashMap==null||service.accessbilityJobHashMap.isEmpty()){
            return;
        }
        String packageName =iStatusBarNotification.getPackageName() ;
        AccessbilityJob accessbilityJob =service.accessbilityJobHashMap.get(packageName);
        if (accessbilityJob!=null&&accessbilityJob.isEnable()){
            accessbilityJob.handleNotificationPosted(iStatusBarNotification);
        }
    }

}
