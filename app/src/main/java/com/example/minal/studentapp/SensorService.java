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
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Attendance_invoke = "1152013,0225541620,7";
    private String Term_Classwork_invoke = "1152013,0225541620,5";
    private SoapPrimitive resultString;
    private News_Site Attendanc;
    private News_Site New;
    private News_Site Termclasswork;
    String data="";
    public SensorService(Context applicationContext) {
        super();

    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Attendanc=new News_Site(this,"Attendance");
        New=new News_Site(this,"News_Site");
        Termclasswork=new News_Site(this,"Term_Classwork");

        AlarmManager am=(AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), SensorService.class);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 600000, pi); // Millisec * Second * Minute

        //NewsAlert();
        AttendanceAlert();
        Term_ClassworkAlert();
        CheckDeadlinesAlert();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent("com.example.minal.studentapp.SenorBroadcast");
        sendBroadcast(broadcastIntent);
    }

    private void CheckDeadlinesAlert()
    {
        List<Deadline> deadlinesList = new ArrayList<>();
        ReadDeadlines allDeadlines = new ReadDeadlines(getApplicationContext(),deadlinesList);

        int numberOfUpkomingDeadlines = 0;
        String ifOnlyOne = null, TextMessage = "";
        //Now we have all deadlines:
        for(int i=0;i<deadlinesList.size();++i)
        {
            if(deadlinesList.get(i).fireAlarm())
            {
                ++numberOfUpkomingDeadlines;
                ifOnlyOne = deadlinesList.get(i).getLabel() + " is Due " +deadlinesList.get(i).getDueDate();
            }
        }
        if(numberOfUpkomingDeadlines == 1) TextMessage = ifOnlyOne;
        else if(numberOfUpkomingDeadlines > 1) TextMessage = "You have "+ numberOfUpkomingDeadlines +" Upcoming Deadlines";

        if(numberOfUpkomingDeadlines >0) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.deadlines1) // notification icon
                    .setContentTitle("Upcoming Deadline") // title for notification
                    .setContentText(TextMessage) // message for notification
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/sound2");
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();
            Intent intent = new Intent(this, LoginActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, Intent.FILL_IN_CATEGORIES);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}