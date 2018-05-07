package com.example.minal.studentapp;

/**
 * Created by lenovoo on 23/04/2018.
 */

        import android.app.AlarmManager;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.Intent;
        import android.media.Ringtone;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.IBinder;
        import android.os.StrictMode;
        import android.support.annotation.Nullable;
        import android.support.annotation.RequiresPermission;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.ksoap2.serialization.SoapPrimitive;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.URL;
        import java.time.Clock;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.Timer;
        import java.util.TimerTask;

/**
 * Created by fabio on 30/01/2016.
 */
public class SensorService extends Service {
    String data="";
    public SensorService(Context applicationContext) {
        super();

    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent("com.example.minal.studentapp.SenorBroadcast");
        sendBroadcast(broadcastIntent);
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}