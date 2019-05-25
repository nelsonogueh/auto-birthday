package com.dulceprime.specialwishes.other_components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by NELSON on 11/23/2017.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
// TODO Auto-generated method stub
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // we start the main activity when the boot is completed
            Intent activityIntent = new Intent(context, com.dulceprime.specialwishes.services.Service_SendingMsg.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(activityIntent);
        }

    }

}
