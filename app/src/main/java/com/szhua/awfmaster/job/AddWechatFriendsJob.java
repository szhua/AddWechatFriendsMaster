package com.szhua.awfmaster.job;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.szhua.awfmaster.Config;
import com.szhua.awfmaster.inter.IStatusBarNotification;
import com.szhua.awfmaster.service.BaseAccessibilityService;
import com.szhua.awfmaster.util.AccessibilityHelper;

import java.util.Random;

/**
 * Created by szhua on 2016/12/15.
 */
public class AddWechatFriendsJob extends BaseAccessbilityJob {


    /**
     * 查找界面的返回 ；
     */
    private boolean isBackSearch =false ;


    /*当前窗口*/
    private static  final  int  WINDOW_NONE =0 ;
    private static  final  int  WINDOW_LANCHER =1  ;
    private static  final  int  WINDOW_ADDFRIEND =2  ;
    private static  final  int  WINDOW_NEWFRIEND =3 ;
    private static  final int WINDOW_SEARCH =4 ;
    private static  final int WINDO_FTS =5 ;
    private static final int WINDOW_CONTACTINFO=6 ;
    private static final int WINDOW_SAY_HELLO =7 ;
    private int currentwindow =WINDOW_NONE ;
    /* 当前界面的class*/
    private String  currentClassName;


    /*微信packageinfo*/
    private PackageInfo packageInfo =null ;

    private BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          updateWechatPackageInfo() ;
        }
    };


    private Handler handler ;

    private Handler getHandler(){
        if(handler!=null){
            return handler ;
        }
        handler =new Handler(Looper.getMainLooper());
        return  handler ;
    }


    @Override
    public void onCreateJob(BaseAccessibilityService service) {
        super.onCreateJob(service);

        /*先进行获得微信的信息*/
        updateWechatPackageInfo();

        /*register receiver!!*/
        IntentFilter filter =new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_DATA_CLEARED);
        getContext().registerReceiver(broadcastReceiver,filter);

    }

    @Override
    public String getPackageName() {
        return Config.WECHAT_PACKAGE_NANE;
    }

    @Override
    public void onStopJob() {
      getContext().unregisterReceiver(broadcastReceiver);
    }
    @Override
    public void onReceiveJob(AccessibilityEvent event) {

        final AccessibilityNodeInfo rootNode =getService().getRootInActiveWindow();
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                currentClassName =String.valueOf(event.getClassName()) ;
                Logger.d(currentClassName);
                if(Config.WECHAT_LANCHER_UI_CLASS_NAME.equals(currentClassName)){
                    currentwindow =WINDOW_LANCHER;

                }else if (Config.WECHAT_ADDFRIEND_UI_CLASS.equals(currentClassName)){

                    currentwindow =WINDOW_ADDFRIEND ;

                }else if(Config.WECHAT_FTSADDFRIEND_UICLASS.equals(currentClassName)){
                    currentwindow =WINDOW_SEARCH ;
                }else if(Config.WECHAT_NEWFRIEND_UI_CLASS.equals(currentClassName)){


                    currentwindow=WINDOW_NEWFRIEND ;
                }else if(Config.WECHAT_FTSMAINUI.equals(currentClassName)){
                    currentwindow =WINDO_FTS ;
                }else if(Config.WECHAT_CONACTINFO_UI.equals(currentClassName)){
                    currentwindow=WINDOW_CONTACTINFO ;
                }else if(Config.WECHAT_SAY_HELLEO_UI.equals(currentClassName)){
                    currentwindow =WINDOW_SAY_HELLO ;
                }
                break;



            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                /*处于首页的时候*/

                if(currentwindow==WINDOW_LANCHER){

                    //更多按钮出现了
                    if("android.widget.ListView".equals(event.getClassName())){
                        Logger.e("搜索");
                       getHandler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               AccessibilityNodeInfo findMoreNode =AccessibilityHelper.findNodeInfosByText(rootNode ,"搜索") ;
                               AccessibilityHelper.performClick(findMoreNode);
                           }
                       },2000);

                    }
                   /*  处于添加朋友的界面时 ； */
                }else if(currentwindow==WINDOW_ADDFRIEND){
                    Logger.d("处于添加朋友的界面");


                       /*处于新的朋友的界面*/
                }else if(currentwindow==WINDOW_NEWFRIEND){


                     /*处于搜索的界面*/
                }else if(currentwindow==WINDOW_SEARCH){

                /*处于查找界面的时候*/
                }else if(currentwindow==WINDO_FTS){
                    Logger.d(event.getClassName());
                  if("android.widget.EditText".equals(event.getClassName())) {
                      AccessibilityNodeInfo searchNodeInfo = AccessibilityHelper.findNodeInfosById(rootNode, "com.tencent.mm:id/g_");
                      AccessibilityHelper.performInput(searchNodeInfo, "deli_zhang", getContext());
                  }

                  if("android.widget.TextView".equals(event.getClassName())||"android.widget.LinearLayout".equals(event.getClassName())||"android.widget.ListView".equals(event.getClassName())){
                      AccessibilityNodeInfo getNodeinfo =AccessibilityHelper.findNodeInfosByKeyword(rootNode,"查找微信号:","查找手机/QQ号:");
                      AccessibilityHelper.performClick(getNodeinfo);
                  }

                    if(isBackSearch){
                        AccessibilityNodeInfo backNode =AccessibilityHelper.findNodeInfosById(rootNode,"com.tencent.mm:id/g7") ;
                        AccessibilityHelper.performClick(backNode);
                        isBackSearch =false ;
                    }

                    if(AccessibilityHelper.findNodeInfosByText(rootNode,"操作过于频繁，请稍后再试。")!=null){
                        isBackSearch =true;
                        getService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        getService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        getService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

                    }



             /*处于联系人详情界面的时候*/
                }else if(currentwindow==WINDOW_CONTACTINFO){
                    if("android.widget.TextView".equals(event.getClassName())){
                    AccessibilityNodeInfo addFriendNode =AccessibilityHelper.findNodeInfosById(rootNode,"com.tencent.mm:id/abv") ;
                    AccessibilityHelper.performClick(addFriendNode);
                    }
                    /*处于添加联系人的界面*/
                }else if(currentwindow==WINDOW_SAY_HELLO){
                    AccessibilityNodeInfo sendRequestNode =AccessibilityHelper.findNodeInfosById(rootNode,"com.tencent.mm:id/fx") ;
                    AccessibilityHelper.performClick(sendRequestNode);
                }
                break;
        }
   }
    @Override
    public boolean isEnable(){
        return true;
    }

    // TODO: 2016/12/15  reslove notification!!
    @Override
    public void handleNotificationPosted(IStatusBarNotification iStatusBarNotification) {

    }

    /*更新微信包的信息*/
    private void updateWechatPackageInfo() {
        try {
            packageInfo =getContext().getPackageManager().getPackageInfo(Config.WECHAT_PACKAGE_NANE,0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获得微信的版本号；
     * @return
     */
    private int getWechatVersion(){
        if(packageInfo==null){
            return  0 ;
        }
        return  packageInfo.versionCode;
    }
}
