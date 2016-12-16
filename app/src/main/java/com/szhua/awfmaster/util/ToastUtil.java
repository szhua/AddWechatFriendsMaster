package com.szhua.awfmaster.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.szhua.awfmaster.R;

/**
 * Created by szhua on 2016/12/15.
 */
public class ToastUtil {
    private static Toast toast ;

    /**
     *
     * @param context
     * @param isLongToast
     * @param  text
     */
    @NonNull
    public static  void showToast(Context context,String text ,boolean isLongToast ){
        if(TextUtils.isEmpty(text)){
            return;
        }
        if (toast==null){
            toast =new Toast(context);
        }else{
            toast.cancel();
        }
        TextView textView =new TextView(context) ;
        textView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        textView.setPadding(20,10,20,10);
        textView.setText(text);
        toast.setView(textView);
        if (isLongToast){
            toast.setDuration(Toast.LENGTH_LONG);
        }else{
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

}
