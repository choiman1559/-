package com.mask.uses.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class BootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = context.getSharedPreferences("MaskValues",MODE_PRIVATE);

        if(Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)
                && prefs.getBoolean("ServiceEnabled",false)) {
            NotificationClass.Notification.Notify(context);
            new NotificationClass.Alarm(context);
            NotificationClass.Alarm.setAlarm();
        }
    }
}
