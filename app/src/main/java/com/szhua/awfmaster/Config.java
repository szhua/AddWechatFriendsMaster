package com.szhua.awfmaster;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by szhua on 2016/12/15.
 */
public class Config {

    public static  final  String TAG ="leilei";

    public static final String PREFERENCE_NAME = "config";
    public static final int DEFAUL_SHAREPRECENCES_MODE =0 ;
    public static final String ACTION_ADDFRIENDS_SERVICE_CONNECT ="com.szhua.awfmaster.action_add_friends_service_isconnected" ;
    public static final String ACTION_ADDFRIENDS_NOTIFICATION_SERVICE_CONNECT ="com.szhua.awfmaster.action_addfriends_notification_service_connect" ;


    public static final String KEY_ABOUT_THIS  ="KEY_ABOUT_THIS" ;
    public static final String KEY_START_WECHAT  ="KEY_START_WECHAT" ;
    public static final String KEY_ENABLE_WECHAT ="KEY_ENABLE_WECHAT" ;



    public static final String WECHAT_PACKAGE_NANE ="com.tencent.mm"  ;
    public static final String WECHAT_LANCHER_UI_CLASS_NAME ="com.tencent.mm.ui.LauncherUI" ;
    public static final String  WECHAT_ADDFRIEND_UI_CLASS ="com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI" ;
    public static final String  WECHAT_FTSADDFRIEND_UICLASS ="com.tencent.mm.plugin.search.ui.FTSAddFriendUI" ;
    public static final String  WECHAT_NEWFRIEND_UI_CLASS ="com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI" ;
    public static final String  WECHAT_FTSMAINUI ="com.tencent.mm.plugin.search.ui.FTSMainUI";
    public static final String  WECHAT_CONACTINFO_UI ="com.tencent.mm.plugin.profile.ui.ContactInfoUI" ;
    public static final String  WECHAT_SAY_HELLEO_UI ="com.tencent.mm.plugin.profile.ui.SayHiWithSnsPermissionUI" ;



    private    SharedPreferences sharedPreferences ;



    /*simple mode*/
    private Config (Context context){
        sharedPreferences =context.getSharedPreferences(PREFERENCE_NAME,DEFAUL_SHAREPRECENCES_MODE);
    };
    private static  class  CongfigSingleton{
        private static  Config getConfig(Context context){
            return  new Config(context) ;
        };
    }
    public static Config getConfig(Context context){
        return  CongfigSingleton.getConfig(context);
    }


    // TODO: 2016/12/15  
    /**是否允许自动添加好友！ */
    public  boolean enAbleWechatAddFriend(){
        return  sharedPreferences.getBoolean(KEY_ENABLE_WECHAT,true);
    }
    /*查看是否能够自动添加好友*/
    public void setWechatAddFriendEnable(boolean isEnable){
        sharedPreferences.edit().putBoolean(KEY_ENABLE_WECHAT,isEnable).apply();
    }





}
