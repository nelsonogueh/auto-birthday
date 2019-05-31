package com.dulceprime.specialwishes.other_components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.os.Build;
import android.support.v4.content.ContextCompat;


/**
 * Created by NELSON on 11/23/2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, com.dulceprime.specialwishes.services.Service_SendingMsg.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);

 /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BootCompleteReceiver.this.startForegroundService(intent);

        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(intent);
        }*/
    }
}
