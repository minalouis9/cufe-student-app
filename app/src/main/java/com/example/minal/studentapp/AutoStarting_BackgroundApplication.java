package com.example.minal.studentapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ahmed on 5/3/2018.
 */

public class AutoStarting_BackgroundApplication extends BroadcastReceiver
    {
        int type=0;
        long Duration = 60;
        Alarm alarm = null;
        String ID;
        List<Deadline> deadlinesList;
        public AutoStarting_BackgroundApplication(int T, long D,Context ct,String ID)
        {
            type = T;
            Duration = D;
            this.ID  = ID;
            alarm = new Alarm(type,Duration,ct,ID);

        }


        @Override
        public void onReceive(Context context, Intent intent) {
            alarm = new Alarm(type,Duration,context,this.ID);

            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                alarm.setAlarm(context);
            } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                alarm.setAlarm(context);
            }
            else if (intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)) {
                alarm.setAlarm(context);
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                alarm.setAlarm(context);
            }
        }



        public void setAlarm(Context context)
        {
            SharedPreferences loginPreferences = context.getSharedPreferences("Alarm_secondCounter", MODE_PRIVATE);
            SharedPreferences.Editor loginPrefs_Editor = loginPreferences.edit();

            int count = loginPreferences.getInt("Counter2",1);

            AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, Alarm.class);
            i.setAction("uniqueCode");

            PendingIntent pi = PendingIntent.getBroadcast(context, count++, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1000 * 60*15, pi); // Millisec * Second * Minute
            Toast.makeText(context,"Alarm On Reboot Is Started",Toast.LENGTH_LONG).show();
            alarm.setAlarm(context);

            loginPrefs_Editor.putInt("Counter2", count);
            loginPrefs_Editor.commit();

        }
    }


