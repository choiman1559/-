package com.mask.uses.counter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

class NotificationClass {
    static class Notification {
        static void Notify(Context context) {
            Intent BroacastIntent = new Intent(context, OnReloadClick.class);
            BroacastIntent.setAction("com.mask.uses.counter.MASK_RELOAD").putExtra("code", "666");
            PendingIntent BroadcastPending = PendingIntent.getBroadcast(context, 666, BroacastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent mIntent = new Intent(context, MainActivity.class);
            PendingIntent mPintent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            SharedPreferences prefs = context.getSharedPreferences("MaskValues", MODE_PRIVATE);

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification);
            contentView.setTextViewText(R.id.toptext, "현재 사용중인 마스크는\n" + prefs.getInt("MaskDays", 0) + "일 째 사용중입니다!");
            contentView.setTextViewText(R.id.textview17, context.getString(R.string.ChangeMask));
            contentView.setOnClickPendingIntent(R.id.textview17, BroadcastPending);
            contentView.setImageViewResource(R.id.reload_icon, R.drawable.ic_autorenew_black_24dp);
            contentView.setOnClickPendingIntent(R.id.reload_icon, BroadcastPending);
            contentView.setImageViewResource(R.id.mask_icon, R.drawable.ic_dentist_mask);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, context.getString(R.string.Channel_id))
                            .setSmallIcon(R.drawable.ic_dentist_mask)
                            .setContent(contentView)
                            .setContentIntent(mPintent)
                            .setOngoing(true);

            NotificationManager mManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT > 26) {
                NotificationChannel mChannel = new NotificationChannel(context.getString(R.string.Channel_id), context.getString(R.string.Channel_name), NotificationManager.IMPORTANCE_DEFAULT);
                assert mManager != null;
                mManager.createNotificationChannel(mChannel);
            }
            assert mManager != null;
            mManager.notify(R.string.Channel_id, mBuilder.build());


        }

        static void Cancel(Context context) {
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            assert nm != null;
            nm.cancelAll();
        }
    }

    static class Alarm {
        private static AlarmManager alarmMgr;
        @SuppressLint("StaticFieldLeak")
        static Context context;
        static PendingIntent BroadcastPending;

        Alarm(Context context) {
            Alarm.context = context;
            Intent BroacastIntent = new Intent(Alarm.context, DayLaterClass.class).putExtra("code", "999");
            BroacastIntent.setAction("com.mask.uses.counter.DAY_LATER");
            BroadcastPending = PendingIntent.getBroadcast(Alarm.context, 999, BroacastIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        static void setAlarm() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE,0);

            alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            assert alarmMgr != null;
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, BroadcastPending);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), BroadcastPending);
            }
            context.getPackageManager().setComponentEnabledSetting(
                    new ComponentName(context, BootComplete.class),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }

        static void cancelAlarm() {
            if (alarmMgr != null) alarmMgr.cancel(BroadcastPending);
        }
    }
}
