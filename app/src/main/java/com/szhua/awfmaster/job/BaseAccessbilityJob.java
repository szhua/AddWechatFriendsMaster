package com.szhua.awfmaster.job;

import android.content.Context;
import android.graphics.Bitmap;

import com.szhua.awfmaster.Config;
import com.szhua.awfmaster.service.AddWechatFriendsService;
import com.szhua.awfmaster.service.BaseAccessibilityService;

/**
 * Created by szhua on 2016/12/15.
 */
public abstract class BaseAccessbilityJob  implements  AccessbilityJob{

   private BaseAccessibilityService baseAccessibilityService ;

    @Override
    public void onCreateJob(BaseAccessibilityService service) {
        this.baseAccessibilityService =service ;
    }

    public Context getContext(){
        if(baseAccessibilityService!=null){
            return  baseAccessibilityService.getApplicationContext() ;
        }
        return  null ;
    }
    public Config getConfig(){
        if(baseAccessibilityService!=null)
        return  baseAccessibilityService.getConfig() ;
        return  null ;
    };
    public BaseAccessibilityService getService(){
        return  baseAccessibilityService ;
    }

}
