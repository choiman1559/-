package com.mask.uses.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class BootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("MaskValues",MODE_PRIVATE);

        if(prefs.getBoolean("onoff",false)) {
            NotificationClass.Notification.Notify(context);
            new NotificationClass.Alarm(context);
            NotificationClass.Alarm.setAlarm();
        }
    }
}
