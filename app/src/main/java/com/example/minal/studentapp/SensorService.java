package com.example.minal.studentapp;

/**
 * Created by lenovoo on 23/04/2018.
 */

        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.os.IBinder;
        import android.support.annotation.Nullable;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;
        import android.widget.Toast;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import org.ksoap2.serialization.SoapPrimitive;

        import java.util.Timer;
        import java.util.TimerTask;

/**
 * Created by fabio on 30/01/2016.
 */
public class SensorService extends Service {
    private String Attendance_invoke = "1152013,0225541620,7";
    private SoapPrimitive resultString;
    String da=" ";
    String SingleParsed_Week=" ";
    public SensorService(Context applicationContext) {
        super();

    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        AttendanceAlert();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*Intent restartService=new Intent(getApplicationContext(),this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);*/
        Intent broadcastIntent = new Intent("com.example.minal.studentapp.SenorBroadcast");
        sendBroadcast(broadcastIntent);
    }
/*
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent broadcastIntent = new Intent("com.example.minal.studentapp.SenorBroadcast");
        sendBroadcast(broadcastIntent);
        stoptimertask();
        Intent restartService=new Intent(getApplicationContext(),this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);
        super.onTaskRemoved(rootIntent);
    }*/


    public void AttendanceAlert() {
        SOAP_Access serverAccessClass = SOAP_Access._getInstance();

       resultString = serverAccessClass.getResponse(Attendance_invoke);

        try {
            da = resultString.toString();
            JSONObject JBO_AllData = new JSONObject(da);
            JSONObject AbsentObject = (JSONObject) JBO_AllData.get("Absence");
            JSONArray AbsentData = (JSONArray) AbsentObject.get("Entry");
            for (int iterator = 0; iterator < 1; iterator++) {
                JSONObject DataInstance_SubjectData = (JSONObject) AbsentData.get(iterator);
                SingleParsed_Week = DataInstance_SubjectData.get("Course_Name") + "";
            }
            } catch (JSONException e)

            {
                e.printStackTrace();
            }


            NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.att) // notification icon
                .setContentTitle("Notification!") // title for notification
                .setContentText(SingleParsed_Week) // message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(this, Attendance.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,Intent.FILL_IN_COMPONENT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());



    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}