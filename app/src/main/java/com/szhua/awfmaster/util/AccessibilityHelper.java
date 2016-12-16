package com.szhua.awfmaster.util;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

import com.orhanobut.logger.Logger;
import com.szhua.awfmaster.Config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.orhanobut.logger.Logger.d;

/**
 * AddWechatFriendsMaster
 * Create   2016/12/15 13:56;
 * https://github.com/szhua
 *
 * @author sz.hua
 */
public class AccessibilityHelper {

    public static final String EDITTEXT_CLASS ="android.widget.EditText" ;


    /*禁用构造方法*/
    private AccessibilityHelper(){}

    /**
     * 通过id找到控件
     * @param nodeInfo
     * @param resoureId
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo , String resoureId){
        if(TextUtils.isEmpty(resoureId)||nodeInfo==null){
            return  null;
        }
        List<AccessibilityNodeInfo> infos =nodeInfo.findAccessibilityNodeInfosByViewId(resoureId);
        if(infos!=null&&!infos.isEmpty()){
         return    infos.get(0) ;
        }
        return  null ;
    }





    /**
     * 通过文字找控件 ；
     * @param nodeInfo
     * @param text
     * @return
     */
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo ,String text){
        if(TextUtils.isEmpty(text)||nodeInfo==null){return null ;}
        List<AccessibilityNodeInfo> infos =nodeInfo.findAccessibilityNodeInfosByText(text);
        if(infos!=null&&!infos.isEmpty()){
            return  infos.get(0) ;
        }
        return  null ;
    }

    /**
     * 通过关键字进行查找控件
     * @param nodeInfo
     * @param keyword
     * @return
     */
    public static  AccessibilityNodeInfo findNodeInfosByKeyword(AccessibilityNodeInfo nodeInfo ,String... keyword){

        if(nodeInfo==null){return null ;}
        for (String key : keyword) {
         List<AccessibilityNodeInfo> infos =nodeInfo.findAccessibilityNodeInfosByText(key) ;
         if(infos!=null&&!infos.isEmpty()){
             return  infos.get(0) ;
         }
        }
        return  null ;
    }

    /**
     * 通过class查找控件，只能够查找第一层的数据
     * @param nodeInfo
     * @param nodeClass
     * @return
     */
    public static AccessibilityNodeInfo findNodeInfosByClass(AccessibilityNodeInfo nodeInfo ,String nodeClass){
        if(TextUtils.isEmpty(nodeClass)||nodeInfo==null){return  null ;}
        int count =nodeInfo.getChildCount() ;
        if(count>0){
            for (int i = 0;  i < count; i++) {
                AccessibilityNodeInfo childNodeInfo =nodeInfo.getChild(i) ;
                if(nodeClass.equals(childNodeInfo.getClassName())){
                    return  childNodeInfo ;
                }
            }
        }
        return  null ;
    }


    /**
     * 通过id寻找位于第index个的控件 ；
     * @param nodeInfo
     * @param resouresId
     * @param index if postion is 0 ;index is 0 ;
     * @return
     */
     @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
     public static AccessibilityNodeInfo findNodeInfosByIdIndex(AccessibilityNodeInfo nodeInfo , String resouresId , int index){
         if(TextUtils.isEmpty(resouresId)||nodeInfo==null){
             return  null ;
         }
         List<AccessibilityNodeInfo> nodes =nodeInfo.findAccessibilityNodeInfosByViewId(resouresId) ;
         if(nodes!=null&&!nodes.isEmpty()){
            return  (index>=nodes.size())?nodes.get(0):nodes.get(index);
         }
         return  null ;
     }



    /**
     * 通过class找父控件 ；
     * @param nodeInfo
     * @param parentClass
     * @return
     */
    public static AccessibilityNodeInfo findParentNodeInfoByClass(AccessibilityNodeInfo nodeInfo ,String parentClass){
        if(nodeInfo==null||TextUtils.isEmpty(parentClass)){
            return  null ;
        }
        if(parentClass.equals(nodeInfo.getClassName())){
            return  nodeInfo ;
        }
        return  findParentNodeInfoByClass(nodeInfo.getParent(),parentClass) ;
    }

    /*点击控件=========================
    若是不能够点击的话找到父控件并且点击*/
    public static void performClick(AccessibilityNodeInfo nodeInfo){

         if(nodeInfo==null){return;}
         if(nodeInfo.isClickable()){
             nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK) ;
         }else{
             performClick(nodeInfo.getParent());
         }
    }



    /**
     * 模拟用户输入操作；
     * @param nodeInfo
     * @param inputString
     */
    public static  void performInput(final AccessibilityNodeInfo nodeInfo , String inputString , Context context){
        if(nodeInfo==null||TextUtils.isEmpty(inputString)||context==null){
            return;
        }
        if (!EDITTEXT_CLASS.equals(nodeInfo.getClassName())){
            Logger.d("获得控件不是输入框，不能够进行输入文字");
            return;
        }
        Logger.d(inputString);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Bundle arguments =new Bundle() ;
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,inputString);
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments);
        }else{
            /**
             * 将内容复制到剪切板；
             */
            final ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData =ClipData.newPlainText(Config.TAG,inputString);
            clipboardManager.setPrimaryClip(clipData);
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    //获取焦点 ；
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    //输入 ；(粘贴)
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    /*清除数据*/
                    clipboardManager.removePrimaryClipChangedListener(this);
                    clipboardManager.setPrimaryClip(null);
                }
            });
        }
        }
    /*返回主界面*/
    public  static void perFormHomeAction(AccessibilityService service){
        if(service!=null){
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        }
    }
    /*返回操作*/
    public static void perFormBack(AccessibilityService service){
        if(service!=null){
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK) ;
        }
    }


}
