package com.mask.uses.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DayLaterClass extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("broadcast","response_day");
        SharedPreferences prefs = context.getSharedPreferences("MaskValues", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        if(Objects.equals(intent.getStringExtra("code"), "999") &&
                !new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime()).equals(prefs.getString("LastAlarm",""))) {
            NotificationClass.Notification.Cancel(context);

            edit.putString("LastAlarm", new SimpleDateFormat( "yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime()));
            edit.putInt("MaskDays", prefs.getInt("MaskDays", 1) + 1);
            edit.putInt("TotalDays", prefs.getInt("TotalDays", 1) + 1);
            edit.apply();
            NotificationClass.Notification.Notify(context);
        }
    }
}
