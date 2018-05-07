package com.example.minal.studentapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;

/**
 * Created by ahmed on 5/3/2018.
 */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapPrimitive;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ahmed on 5/3/2018.
 */
public class Alarm extends BroadcastReceiver {

    private long Interval_of_Check_Minutes = 1; //this is multiplied by 1000*60 for minuites
    private String Attendance_invoke = "1152013,0225541620,7";
    private String Term_Classwork_invoke = "1152013,0225541620,5";
    private SoapPrimitive resultString;
    private News_Site Attendanc;
    private News_Site New;
    private News_Site Termclasswork;
    String Message;
    private String ID ;
    int TypeOfBackgroundNotification;
    private List<Deadline> deadlinesList;
    private String TextMessage;
    private List<Deadline> deadlineList;
    private  PowerManager.WakeLock wl;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefs_Editor;

    public Alarm() {

    }

    public Alarm(int Type, long DurationMunites, Context ct,String ID_in) {
        this.TypeOfBackgroundNotification = Type;
        Interval_of_Check_Minutes = DurationMunites;
        ID=ID_in;
        ReadDeadlines.LoadAllDeadlines(ct,ID);
        deadlineList = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.

        //Code Here:
        Log.e("Background Info: ", " Background is invoked --------------------------------------------------------------\n-------------------------------------------------------------------------------------\n]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");

        SharedPreferences loginPreferences = context.getSharedPreferences("Deadlines", MODE_PRIVATE);
        SharedPreferences.Editor loginPrefs_Editor = loginPreferences.edit();

        String Message = loginPreferences.getString("Deadlines_Notification_Message","No Deadlines Yet");
        String DueDate = loginPreferences.getString("Deadlines_DueDate", "1/1/2000");
        String DueDateIncrement = loginPreferences.getString("Deadlines_DueDate_Increment", "1");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dueDate =  sdf.parse(DueDate);

            if(dueDate.compareTo(new Date())<=0 && (DueDate.compareTo("1/1/2000")!=0)) //The Deadline is Due!
            {

                Calendar calendar_5DaysEarlierThanToday = Calendar.getInstance(); // this would default to now
                calendar_5DaysEarlierThanToday.setTime(dueDate);
                calendar_5DaysEarlierThanToday.add(Calendar.DAY_OF_MONTH, Integer.parseInt(DueDateIncrement));
                DueDate = sdf.format(calendar_5DaysEarlierThanToday.getTime());


                Message = Message + " is Due " + DueDate;

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.deadlines1) // notification icon
                        .setContentTitle("Upcoming Deadline") // title for notification
                        .setContentText(Message) // message for notification
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
                Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                r.play();
                Intent intent3 = new Intent(context, LoginActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 100, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

/*
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.deadlines1) // notification icon
                .setContentTitle("Upcoming Deadline") // title for notification
                .setContentText(Message) // message for notification
                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
        r.play();
        Intent intent3 = new Intent(context, LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 100, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
*/

        //new NotificationBuilder(context);

        // Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example


    }


    public void setAlarm(Context context) {
        loginPreferences = context.getSharedPreferences("Alarm Counter", MODE_PRIVATE);
        loginPrefs_Editor = loginPreferences.edit();


        int count = loginPreferences.getInt("Counter",1);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        i.setAction("uniqueCode");
        PendingIntent pi = PendingIntent.getBroadcast(context, count++, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, 0, 1000 * 60*15, pi); // Millisec * Second * Minute
        Toast.makeText(context, "Alarm Is Started", Toast.LENGTH_LONG);

        loginPrefs_Editor.putInt("Counter", count);
        loginPrefs_Editor.commit();
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


    private class NotificationBuilder {

        private News_Site Warning;
        private News_Site Gpa;
        private String Warning_invoke;
        private String GPA_invoke;

        public NotificationBuilder(Context cntx) {


            //   initialize(cntx);

            CheckDeadlinesAlert(cntx);

        }

        void initialize(Context context) {
         /*  Warning=new News_Site(context,"Warning");
           Gpa=new News_Site(context,"Gpa");
           loginPreferences = context.getSharedPreferences("loginPrefs", MODE_PRIVATE);
           Attendance_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",7";
           Term_Classwork_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",5";
           Warning_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",8";
           GPA_invoke = loginPreferences.getString("username", "")+","+loginPreferences.getString("password", "")+",2";
       */
        }

        //Notifications Functions:
        private void CheckDeadlinesAlert(Context context) {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.deadlines1) // notification icon
                    .setContentTitle("Upcoming Deadline") // title for notification
                    .setContentText("Minute Passed") // message for notification
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
            Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
            r.play();
            Intent intent = new Intent(context, LoginActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
/*
          String TextMessage;
            try {
                List<Deadline> deadlinesList = new ArrayList<>();
                ReadDeadlines allDeadlines = new ReadDeadlines(context, deadlinesList);

                int numberOfUpkomingDeadlines = 0;
                String ifOnlyOne = null;
                TextMessage = "No Upcoming deadlines ahead";
                //Now we have all deadlines:
                for (int i = 0; i < deadlinesList.size(); ++i) {
                    if (deadlinesList.get(i).fireAlarm()) {
                        ++numberOfUpkomingDeadlines;
                        ifOnlyOne = deadlinesList.get(i).getLabel() + " is Due " + deadlinesList.get(i).getDueDate();
                    }
                }
                if (numberOfUpkomingDeadlines == 1) TextMessage = ifOnlyOne;
                else if (numberOfUpkomingDeadlines > 1)
                    TextMessage = "You have " + numberOfUpkomingDeadlines + " Upcoming Deadlines";

                //if(numberOfUpkomingDeadlines >0)
                {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.deadlines1) // notification icon
                            .setContentTitle("Upcoming Deadline") // title for notification
                            .setContentText(TextMessage) // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, LoginActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }

            } catch (Exception e) {
                Toast.makeText(context, "Deadline Thrown", Toast.LENGTH_LONG).show();
                Log.e("Background Info: ","-----------------------------"+e.getMessage());

            }
*/
            //--------------------------------------------------------------------------------------------

  /*          SOAP_Access serverAccessClass;
            String SoapString;
            try {
                serverAccessClass = SOAP_Access._getInstance();


                resultString = serverAccessClass.getResponse(Attendance_invoke);
                SoapString = resultString.toString();
                SoapString += "\n";
                if (Attendanc.readSavedData() != null) {
                    if (!(Attendanc.readSavedData().equals(SoapString))) {
                        Attendanc.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.attendance1) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("New Absence added") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/open");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());
                    } else {
                        Attendanc.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.attendance1) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("No New Absence added") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/open");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());
                    }

                } else Attendanc.saveData(resultString.toString());

            } catch (Exception e) {
                Toast.makeText(context, "Deadline Thrown", Toast.LENGTH_LONG).show();
            }
            //-------------------------------------------------------------------------------------------------

            try {
                serverAccessClass = SOAP_Access._getInstance();

                resultString = serverAccessClass.getResponse(Term_Classwork_invoke);
                SoapString = resultString.toString();
                SoapString += "\n";
                if (Termclasswork.readSavedData() != null) {
                    if (!(Termclasswork.readSavedData().equals(SoapString))) {
                        Termclasswork.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.coursework2) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("New Classwork result is added") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());
                    } else {
                        Termclasswork.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.coursework2) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("No New Classwork result is added") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());

                    }
                } else Termclasswork.saveData(resultString.toString());
            } catch (Exception e) {
                Toast.makeText(context, "Deadline Thrown", Toast.LENGTH_LONG).show();
            }
            //-------------------------------------------------------------------------------------------

            try {
                serverAccessClass = SOAP_Access._getInstance();

                resultString = serverAccessClass.getResponse(GPA_invoke);
                SoapString = resultString.toString();
                SoapString += "\n";
                if (Gpa.readSavedData() != null) {
                    if (!(Gpa.readSavedData().equals(SoapString))) {
                        Gpa.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.transcript1) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("Final Results are added") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(3, mBuilder.build());
                    } else {
                        Gpa.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.transcript1) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("No Final Results added") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(3, mBuilder.build());
                    }
                } else Gpa.saveData(resultString.toString());
            } catch (Exception e) {
                Toast.makeText(context, "Deadline Thrown", Toast.LENGTH_LONG).show();
            }
            //----------------------------------------------------------

            try {
                serverAccessClass = SOAP_Access._getInstance();

                resultString = serverAccessClass.getResponse(Warning_invoke);
                SoapString = resultString.toString();
                SoapString += "\n";
                if (Warning.readSavedData() != null) {
                    if (!(Warning.readSavedData().equals(SoapString))) {

                        Warning.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.warn) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("Warning!!") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/closed");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(2, mBuilder.build());
                    } else {
                        Warning.saveData(resultString.toString());
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.warn) // notification icon
                                .setContentTitle("CUFE") // title for notification
                                .setContentText("No Warning!!") // message for notification
                                .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/closed");
                        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                        r.play();
                        Intent intent = new Intent(context, LoginActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                        mBuilder.setContentIntent(pi);
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(2, mBuilder.build());
                    }
                } else Warning.saveData(resultString.toString());
            } catch (Exception e) {
                Toast.makeText(context, "Deadline Thrown", Toast.LENGTH_LONG).show();
            }
            //-------------------------------------------------
*/

     /*       try {

                ReadAllNews newsReader = ReadAllNews._get_instance(context);
                List<FeedItem> feedItems = newsReader.getNewFeeds();

                int countNewNews = 0;
                String newNewsDesc = null;

                for (int i = 0; i < feedItems.size(); ++i) {
                    if (newsReader.isNewNews(feedItems.get(i).getDescription())) {
                        ++countNewNews;
                        newNewsDesc = feedItems.get(i).getDescription();
                    }
                }

                //String TextMessage;

                if (countNewNews == 1) TextMessage = newNewsDesc;
                else if (countNewNews > 1) TextMessage = "New " + countNewNews + " News are unseen";
                else {
                    TextMessage = "No Unseen News!";
                }

                //if (countNewNews > 0)
                {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.newsnotifi) // notification icon
                            .setContentTitle("Faculty News") // title for notification
                            .setContentText(TextMessage) // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, LoginActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }

            } catch (Exception e) {
                Toast.makeText(context, "Deadline Thrown", Toast.LENGTH_LONG).show();
            }
*/
        }


        // To notify if there is new absence
        private void AttendanceAlert(Context context) {
            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Attendance_invoke);
            String SoapString = resultString.toString();
            SoapString += "\n";
            if (Attendanc.readSavedData() != null) {
                if (!(Attendanc.readSavedData().equals(SoapString))) {
                    Attendanc.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.attendance1) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("New Absence added") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/open");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                } else {
                    Attendanc.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.attendance1) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("No New Absence added") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/open");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, mBuilder.build());
                }

            } else Attendanc.saveData(resultString.toString());
        }

        private void Term_ClassworkAlert(Context context) {
            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Term_Classwork_invoke);
            String SoapString = resultString.toString();
            SoapString += "\n";
            if (Termclasswork.readSavedData() != null) {
                if (!(Termclasswork.readSavedData().equals(SoapString))) {
                    Termclasswork.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.coursework2) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("New Classwork result is added") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());
                } else {
                    Termclasswork.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.coursework2) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("No New Classwork result is added") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder.build());

                }
            } else Termclasswork.saveData(resultString.toString());


        }

        private void GPAAlert(Context context) {
            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(GPA_invoke);
            String SoapString = resultString.toString();
            SoapString += "\n";
            if (Gpa.readSavedData() != null) {
                if (!(Gpa.readSavedData().equals(SoapString))) {
                    Gpa.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.transcript1) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("Final Results are added") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(3, mBuilder.build());
                } else {
                    Gpa.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.transcript1) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("No Final Results added") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/se");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(3, mBuilder.build());
                }
            } else Gpa.saveData(resultString.toString());

        }

        public void WarningAlert(Context context) {
            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Warning_invoke);
            String SoapString = resultString.toString();
            SoapString += "\n";
            if (Warning.readSavedData() != null) {
                if (!(Warning.readSavedData().equals(SoapString))) {

                    Warning.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.warn) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("Warning!!") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/closed");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(2, mBuilder.build());
                } else {
                    Warning.saveData(resultString.toString());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.warn) // notification icon
                            .setContentTitle("CUFE") // title for notification
                            .setContentText("No Warning!!") // message for notification
                            .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                    Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/closed");
                    Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                    r.play();
                    Intent intent = new Intent(context, Alarm.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                    mBuilder.setContentIntent(pi);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(2, mBuilder.build());
                }
            } else Warning.saveData(resultString.toString());

            //-------------------------------------------------


        }

        private void checkNews(Context context) {

            ReadAllNews newsReader = ReadAllNews._get_instance(context);
            List<FeedItem> feedItems = newsReader.getNewFeeds();

            int countNewNews = 0;
            String newNewsDesc = null;

            for (int i = 0; i < feedItems.size(); ++i) {
                if (newsReader.isNewNews(feedItems.get(i).getDescription())) {
                    ++countNewNews;
                    newNewsDesc = feedItems.get(i).getDescription();
                }
            }

            String TextMessage = null;
            if (countNewNews == 1) TextMessage = newNewsDesc;
            else if (countNewNews > 1) TextMessage = "New " + countNewNews + " News are unseen";
            else {
                TextMessage = "No Unseen News!";
            }

            //if (countNewNews > 0)
            {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.newsnotifi) // notification icon
                        .setContentTitle("Faculty News") // title for notification
                        .setContentText(TextMessage) // message for notification
                        .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
                Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
                Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
                r.play();
                Intent intent = new Intent(context, Alarm.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, Intent.FILL_IN_CATEGORIES);
                mBuilder.setContentIntent(pi);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }


        }
    }



    public class AsyncCallWS_Alarm extends AsyncTask<Void, Void, Void> {

    private Context context;

        public AsyncCallWS_Alarm(Context ctx)
        {
            context= ctx;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            int numberOfUpkomingDeadlines = 0;
            String ifOnlyOne = null;
            TextMessage = "";
            //Now we have all deadlines:
            for (int i = 0; i < deadlinesList.size(); ++i) {
                if (deadlinesList.get(i).fireAlarm()) {
                    ++numberOfUpkomingDeadlines;
                    ifOnlyOne = deadlinesList.get(i).getLabel() + " is Due " + deadlinesList.get(i).getDueDate();
                }
            }
            if (numberOfUpkomingDeadlines == 1) TextMessage = ifOnlyOne;
            else if (numberOfUpkomingDeadlines > 1)
                TextMessage = "You have " + numberOfUpkomingDeadlines + " Upcoming Deadlines";

            return null;
        }

        @Override
        protected void onPreExecute() {
            //Log.i(TAG, "onPreExecute");

            TextMessage = "None";
            try {
                String FileName;

                if(ID==null||ID.length()==0) //No Data Read
                {//Try using Parser:
                    return;
                }

                FileInputStream ReadFile = context.openFileInput( ID +"_"+ "AllFiles");

                InputStreamReader Reader = new InputStreamReader(ReadFile);
                BufferedReader Readings_Buffer = new BufferedReader(Reader);
                int i=0;
                while ((FileName = Readings_Buffer.readLine()) != null)
                {
                    deadlineList.add( new Deadline(FileName, context,++i));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //this.Invoke_Toast("file not found",cntx);
            } catch (IOException e) {
                e.printStackTrace();
                //this.Invoke_Toast("Could not read Reminder!\nTry changing the Label and try again",cntx);
            }

        }

        @Override
        protected void onPostExecute(Void result) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.deadlines1) // notification icon
                    .setContentTitle("Upcoming Deadline") // title for notification
                    .setContentText(TextMessage) // message for notification
                    .setAutoCancel(true).setDefaults(Notification.DEFAULT_VIBRATE).setOnlyAlertOnce(true); // clear notification after click
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/sound2");
            Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
            r.play();
            Intent intent3 = new Intent(context, LoginActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 100, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());

            wl.release();
        }

        }

    }