package com.mask.uses.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Objects;

public class OnReloadClick extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Response","Receive");

        if(Objects.equals(intent.getStringExtra("code"), "666")) {
            SharedPreferences prefs = context.getSharedPreferences("MaskValues", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            NotificationClass.Notification.Cancel(context);
            edit.putInt("TotalMask", prefs.getInt("TotalMask", 0) + 1);
            edit.putInt("MaskDays", 1);
            edit.apply();
            NotificationClass.Notification.Notify(context);
        }
    }
}
