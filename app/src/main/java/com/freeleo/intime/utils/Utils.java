package com.freeleo.intime.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Description: <对此类的描述> 
 * Author: lizhen
 * Create Date: 22/03/2018
 * Modified By: lizhen
 * Modified Date: 22/03/2018
 * Why & What is modified: <修改原因描述> 
 */
public class Utils {
    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning(Context context,final String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }
}
