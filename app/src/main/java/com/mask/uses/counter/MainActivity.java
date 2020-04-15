package com.mask.uses.counter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static int Layoutmode;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_debug_main); Layoutmode = 0;
        setContentView(R.layout.activity_main); Layoutmode = 1;

        SharedPreferences prefs = getSharedPreferences("MaskValues", MODE_PRIVATE);

        if (prefs.getInt("MaskDays", 0) == 0)
            prefs.edit().putInt("MaskDays", 1).apply();

        if (prefs.getInt("TotalDays", 0) == 0)
            prefs.edit().putInt("TotalDays", 1).apply();

        if (prefs.getInt("TotalMask", 0) == 0)
            prefs.edit().putInt("TotalMask", 1).apply();

        if("".equals(prefs.getString("LastAlarm","")))
            prefs.edit().putString("LastAlarm", new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(Calendar.getInstance().getTime())).apply();

        if (Layoutmode == 1) {
            NotificationClass.Notification.Notify(MainActivity.this);
            new NotificationClass.Alarm(this);
            NotificationClass.Alarm.setAlarm();

            TextView TotalMask,MaskDays,Avarge_mask,Avarge_days;

            TotalMask = findViewById(R.id.TotalMask);
            MaskDays = findViewById(R.id.MaskDays);
            Avarge_days = findViewById(R.id.Avarge_days);
            Avarge_mask = findViewById(R.id.Avarge_mask);

            TotalMask.setText(incode(prefs.getInt("TotalMask", 0)));
            MaskDays.setText(incode( prefs.getInt("MaskDays", 0)));

            float avarge_mask = (float) prefs.getInt("TotalMask", 0) / (float) prefs.getInt("TotalDays", 0);
            Avarge_mask.setText(NaN(incode(avarge_mask)));

            float avarge_days =  (float) prefs.getInt("TotalDays", 0) / (float) prefs.getInt("TotalMask", 0);
            Avarge_days.setText(NaN(incode(avarge_days)));

            ImageView exit = findViewById(R.id.exit);
            exit.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("앱 종료").setMessage("이 앱을 종료하시겠습니까?");

                builder.setPositiveButton("확인", (dialog, id) -> {
                    NotificationClass.Notification.Cancel(MainActivity.this);
                    new NotificationClass.Alarm(MainActivity.this);
                    NotificationClass.Alarm.cancelAlarm();
                    finish();
                });

                builder.setNegativeButton("취소", (dialog, id) -> { });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });
        }

        if (Layoutmode == 0) {
            findViewById(R.id.button).setOnClickListener(v -> {
                NotificationClass.Notification.Notify(MainActivity.this);

                new NotificationClass.Alarm(this);
                NotificationClass.Alarm.setAlarm();

                recreate();
            });

            findViewById(R.id.button2).setOnClickListener(v -> {
                NotificationClass.Notification.Cancel(MainActivity.this);

                new NotificationClass.Alarm(this);
                NotificationClass.Alarm.cancelAlarm();

                recreate();
            });

            TextView TotalMask = findViewById(R.id.textView);
            TextView TotalDays = findViewById(R.id.textView2);
            TextView MaskDays = findViewById(R.id.textView3);
            TextView avarge = findViewById(R.id.textView4);

            TotalMask.setText("총 사용한 마스크 개수 : " + prefs.getInt("TotalMask", 0) + "(개)");
            TotalDays.setText("마스크 사용 총일 : " + prefs.getInt("TotalDays", 0) + "(일)");
            MaskDays.setText("현재 마스크 사용 일수 : " + prefs.getInt("MaskDays", 0) + "(일)");

            float avargeNum1 = (float) prefs.getInt("TotalDays", 0) / (float) prefs.getInt("TotalMask", 0);
            avarge.setText("평균 마스크 사용 갯수 : " + (avargeNum1 % 1 == 0 ? (int) avargeNum1 : avargeNum1) + "(개/일)");
        }
    }

    String incode(int input) {
        if(input < 10) return "0" + input;
        else return Integer.toString(input);
    }

    String incode(float input) {
        if(input % 1.0 == 0.0) return (input < 10 ? "0" : "") + Math.round(input);
        else return String.format(Locale.getDefault(),"%.1f",input);
    }

    String NaN(String str) {
        return str.equals("NaN") || str.equals("Infinity") ? "00" : str;
    }
}