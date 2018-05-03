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
        import android.content.SharedPreferences;
        import android.media.Ringtone;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.Handler;
        import android.os.IBinder;
        import android.os.StrictMode;
        import android.support.annotation.Nullable;
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
        import java.util.Calendar;
        import java.util.Timer;
        import java.util.TimerTask;

/**
 * Created by fabio on 30/01/2016.
 */
public class SensorService extends Service {
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Attendance_invoke = " ";
    private String Term_Classwork_invoke = " ";
    private String Warning_invoke = " ";
    private String GPA_invoke = " ";
    public static SharedPreferences loginPreferences=null;

    private SoapPrimitive resultString;
    private News_Site Attendanc;
    private News_Site Termclasswork;

    private News_Site Warning;
    private News_Site Gpa;
    public Handler handler = null;
    public static Runnable runnable = null;

    private ConnectionDetector cdx;
    String data="";
    public SensorService(Context applicationContext) {
        super();

    }

    public SensorService() {
    }
   /* @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                AttendanceAlert();
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);

    }*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Attendanc=new News_Site(this,"Attendance");
        Termclasswork=new News_Site(this,"Term_Classwork");
        Warning=new News_Site(this,"Warning");
        Gpa=new News_Site(this,"Gpa");
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        Attendance_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",7";
        Term_Classwork_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",5";
        Warning_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",8";
        GPA_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",2";

       /*Calendar cal = Calendar.getInstance();
        PendingIntent pintent = PendingIntent
                .getBroadcast(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every hour
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pintent);//1000*60 = 5 minute
        /*AlarmManager mgr = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this,
                SensorService.class);
        //PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(), req, Intent.parseIntent(), 0);
        PendingIntent pintent = PendingIntent
                .getService(this, 0,notificationIntent , 0);
        mgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                System.currentTimeMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pintent);*/
        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, SensorService.class),PendingIntent.FLAG_UPDATE_CURRENT);

// Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
       /*handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                AttendanceAlert();
                Term_ClassworkAlert();
                WarningAlert();
                handler.postDelayed(runnable, 60000);
            }
        };

      //  handler.postDelayed(runnable, 15000);*/

        //NewsAlert();<<<<<< HEAD

        //Term_ClassworkAlert();
       //

        cdx = new ConnectionDetector(this);
        if (cdx.isConnected() & !Attendance_invoke.equals(",,7") ) {
            AttendanceAlert();
            Term_ClassworkAlert();
            WarningAlert();
            GPAAlert();
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent("com.example.minal.studentapp.SenorBroadcast");
        sendBroadcast(broadcastIntent);
    }

// To notify if there is new absence
    public void AttendanceAlert() {
        SOAP_Access serverAccessClass = SOAP_Access._getInstance();

       resultString = serverAccessClass.getResponse(Attendance_invoke);
       String  SoapString= resultString.toString();
        SoapString+="\n";
        if(Attendanc.readSavedData()!=null) {
            if (!(Attendanc.readSavedData().equals(SoapString))) {
                Attendanc.saveData(resultString.toString());
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.attendance1) // notification icon
                        .setContentTitle("CUFE") // title for notification
                        .setContentText("New Absence added") // message for notification
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/open");
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                r.play();
                Intent intent = new Intent(this, LoginActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_CATEGORIES);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }else Attendanc.saveData(resultString.toString());




    }
    public void Term_ClassworkAlert() {
        SOAP_Access serverAccessClass = SOAP_Access._getInstance();

        resultString = serverAccessClass.getResponse(Term_Classwork_invoke);
        String  SoapString= resultString.toString();
        SoapString+="\n";
        if(Termclasswork.readSavedData()!=null) {
            if (!(Termclasswork.readSavedData().equals(SoapString))) {
                Termclasswork.saveData(resultString.toString());
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.coursework2) // notification icon
                        .setContentTitle("CUFE") // title for notification
                        .setContentText("New Classwork result is added") // message for notification
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/se");
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                r.play();
                Intent intent = new Intent(this, LoginActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_CATEGORIES);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
            }
        }else Termclasswork.saveData(resultString.toString());




    }

    public void GPAAlert() {
        SOAP_Access serverAccessClass = SOAP_Access._getInstance();

        resultString = serverAccessClass.getResponse(GPA_invoke);
        String  SoapString= resultString.toString();
        SoapString+="\n";
        if(Gpa.readSavedData()!=null) {
            if (
                    (Gpa.readSavedData().equals(SoapString))) {
                Gpa.saveData(resultString.toString());
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.transcript1) // notification icon
                        .setContentTitle("CUFE") // title for notification
                        .setContentText("Final Results are added") // message for notification
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/se");
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                r.play();
                Intent intent = new Intent(this, LoginActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_CATEGORIES);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(3, mBuilder.build());
            }
        }else Gpa.saveData(resultString.toString());




    }

    public void WarningAlert() {
        SOAP_Access serverAccessClass = SOAP_Access._getInstance();

        resultString = serverAccessClass.getResponse(Warning_invoke);
        String  SoapString= resultString.toString();
        SoapString+="\n";
        if(Warning.readSavedData()!=null) {
            if (!(Warning.readSavedData().equals(SoapString))) {

                Warning.saveData(resultString.toString());
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warn) // notification icon
                        .setContentTitle("CUFE") // title for notification
                        .setContentText("Warning!! Enzaaaaaar") // message for notification
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/closed");
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
                r.play();
                Intent intent = new Intent(this, LoginActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_CATEGORIES);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(2, mBuilder.build());
            }
        }else Warning.saveData(resultString.toString());




    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}