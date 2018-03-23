package com.freeleo.intime.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.freeleo.intime.Constants.Constants;

/**
 * Description: <对此类的描述> 
 * Author: lizhen
 * Create Date: 21/03/2018
 * Modified By: lizhen
 * Modified Date: 21/03/2018
 * Why & What is modified: <修改原因描述> 
 */
public class NotificationCenter {
    public static  void showCountDown(Context context,String title,String text){
        Notification.Builder notificationBuilder = new Notification.Builder(
                context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.ic_launcher));

        Notification notification = null;
        if(Build.VERSION.SDK_INT>=16){
            notification=notificationBuilder.build();
        }else{
            notification=notificationBuilder.getNotification();
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFY_ID, notification);
    }

    public static void hide(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
