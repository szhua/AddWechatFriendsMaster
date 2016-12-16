package com.szhua.awfmaster.service;

import android.accessibilityservice.AccessibilityService;

import com.szhua.awfmaster.Config;

/**
 * Created by szhua on 2016/12/15.
 */
public abstract class BaseAccessibilityService  extends AccessibilityService {

   public  abstract Config getConfig();

}
