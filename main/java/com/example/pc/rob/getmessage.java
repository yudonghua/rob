package com.example.pc.rob;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class getmessage extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification=sbn.getNotification();
        Bundle extras = mNotification.extras;

//        Log.i("sbn.toString()", "open"+"-----"+sbn.toString());
//        Log.i("class", "open"+"-----"+mNotification.getClass());
//        Log.i("extras", "open"+"-----"+extras.toString());
//        Log.i("EXTRA_TITLE", "open"+"-----"+extras.getString(Notification.EXTRA_TITLE));
//        Log.i("EXTRA_INFO_TEXT", "open"+"-----"+extras.getString(Notification.EXTRA_INFO_TEXT));
//        Log.i("CONVERSATION_TITLE", "open"+"-----"+extras.getString(Notification.EXTRA_CONVERSATION_TITLE));
//        Log.i("EXTRA_BIG_TEXT", "open"+"-----"+extras.getString(Notification.EXTRA_BIG_TEXT));
//
//        Log.i("EXTRA_TEXT", "open"+"-----"+extras.getString(Notification.EXTRA_TEXT));
//        Log.i("EXTRA_TITLE_BIG", "open"+"-----"+extras.getString(Notification.EXTRA_TITLE_BIG));
//        Log.i("EXTRA_SUB_TEXT", "open"+"-----"+extras.getString(Notification.EXTRA_SUB_TEXT));
        if(sbn.toString()!=null&&extras!=null){
            if(extras.getString(Notification.EXTRA_TEXT).contains("红包")&&extras.toString().contains("com.tencent.mobileqq")){
//            Intent intent =new Intent(Intent.ACTION_MAIN);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//            ComponentName cn=new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.SplashActivity");
//            intent.setComponent(cn);
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                startActivity(intent);
            }
            if(extras.getString(Notification.EXTRA_TEXT).contains("红包")&&extras.toString().contains("com.tencent.mm")){
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                startActivity(intent);
            }
        }



    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("zpf", "shut"+"-----"+sbn.toString());

    }

}





