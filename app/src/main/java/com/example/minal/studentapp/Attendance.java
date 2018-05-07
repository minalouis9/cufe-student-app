package com.example.minal.studentapp;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;
import android.content.Context;
import android.content.Intent;

public class Attendance extends AppCompatActivity {

    private String TAG = "Response to Class Grades: ";
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Attendance_invoke = ID+","+Password+",7";
    private SoapPrimitive resultString;

    private String data = null;
    private String dataParsed_SubjectName = "";
    private String SingleParsed_SubjectName = "";
    private String dataParsed_Week = "";
    private String SingleParsed_Week = "";
    private String dataParsed_Day = "";
    private String SingleParsed_Day = "";
    private String dataParsed_Session = "";
    private String SingleParsed_Session = "";
    private String SingleParsed_Begin = "";
    private String SingleParsed_End = "";
    private String[] Subjects;
    private int[] RemainingHours;
    private String dataParsed_CourseName = "";
    private String dataParsed_AbsenceHours = "";
    TextView textView_SubjectName;
    TextView textView_Day;
    TextView textView_Week;
    TextView textView_Session;
    TextView textView_DayTilte;
    TextView textView_WeekTilte;
    TextView textView_SessionTilte;
    Switch aSwitch;
    Intent mServiceIntent;
    //private SensorService mSensorService;

    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        AsyncCallWS_ReadAttendance gradesReader = new AsyncCallWS_ReadAttendance();
        gradesReader.execute();
        Subjects=new String[8];
        RemainingHours=new int[8];
        Subjects[0]="Seminar-1";
        Subjects[1]="Software Engineering";
        Subjects[2]="Microprocessor Systems-2";
        Subjects[3]="Computer Architecture";
        Subjects[4]="Civil Engineering";
        Subjects[5]="Signal Analysis";
        Subjects[6]="Mechanical Engineering";
        Subjects[7]="Economics";
        textView_SubjectName = (TextView) findViewById(R.id.text3);
        textView_Day = (TextView) findViewById(R.id.text1);
        textView_Week = (TextView) findViewById(R.id.text2);
        textView_Session = (TextView) findViewById(R.id.text4);
        textView_DayTilte = (TextView) findViewById(R.id.textView1);
        textView_WeekTilte = (TextView) findViewById(R.id.textView2);
        textView_SessionTilte = (TextView) findViewById(R.id.textView4);
        aSwitch=(Switch) findViewById(R.id.Switch);
        ctx = this;

       /* mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        */
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }


    private class AsyncCallWS_ReadAttendance extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            Get_Attendance();
            for (int i = 0; i < Subjects.length; i++) {
                dataParsed_CourseName = dataParsed_CourseName + Subjects[i] + "\n";
                if (Subjects[i].length() < 40)
                    dataParsed_CourseName = dataParsed_CourseName + "\n";
            }

            for (int i = 0; i < Subjects.length; i++) {
                dataParsed_AbsenceHours = dataParsed_AbsenceHours + Integer.toString(RemainingHours[i]) + "\n\n";

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            textView_SubjectName.setText(dataParsed_SubjectName);
            textView_Day.setText(dataParsed_Day);
            textView_Week.setText(dataParsed_Week);
            textView_Session.setText(dataParsed_Session);
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b)
                    {


                        textView_SubjectName.setText(dataParsed_CourseName);
                        textView_Day.setText(dataParsed_AbsenceHours);
                        textView_Week.setText("");
                        textView_Session.setText("");
                        textView_DayTilte.setText("");
                        textView_WeekTilte.setText("Absent Hours");
                        textView_SessionTilte.setText("");
                    }
                    else{
                        textView_SubjectName.setText(dataParsed_SubjectName);
                        textView_Day.setText(dataParsed_Day);
                        textView_Week.setText(dataParsed_Week);
                        textView_Session.setText(dataParsed_Session);
                        textView_DayTilte.setText("Day");
                        textView_WeekTilte.setText("Week");
                        textView_SessionTilte.setText("Session");
                    }
                }
            });
        }


        private void Get_Attendance() {

            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Attendance_invoke);

            try {
                data = resultString.toString();
                JSONObject JBO_AllData = new JSONObject(data);
                JSONObject AbsentObject = (JSONObject) JBO_AllData.get("Absence");
                JSONArray AbsentData = (JSONArray) AbsentObject.get("Entry");
                for (int iterator = 0; iterator < AbsentData.length(); iterator++) {
                    JSONObject DataInstance_SubjectData = (JSONObject) AbsentData.get(iterator);
                    SingleParsed_Week = DataInstance_SubjectData.get("Week") + "";
                    SingleParsed_Day = DataInstance_SubjectData.get("Day") + "";
                    SingleParsed_Session = DataInstance_SubjectData.get("Session") + "";
                    SingleParsed_SubjectName = DataInstance_SubjectData.get("Course_Name") + "";


                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName + "\n\n";
                    dataParsed_Day = dataParsed_Day + SingleParsed_Day + "\n\n";
                    dataParsed_Week = dataParsed_Week + SingleParsed_Week + "\n\n";
                    dataParsed_Session = dataParsed_Session + SingleParsed_Session + "\n\n";

                    if (SingleParsed_SubjectName.length() > 18) {
                        dataParsed_Day += "\n";
                        dataParsed_Week += "\n";
                        dataParsed_Session += "\n";
                    }
                    for (int i = 0; i < Subjects.length; i++) {
                        if (SingleParsed_SubjectName.length()==Subjects[i].length() && SingleParsed_SubjectName.matches(Subjects[i])) {
                            SingleParsed_End = DataInstance_SubjectData.get("End_Time") + "";
                            SingleParsed_Begin = DataInstance_SubjectData.get("Begin_Time") + "";
                            String Endtime=SingleParsed_End.substring(0,2);
                            String Begintime=SingleParsed_Begin.substring(0,2);
                            if(Endtime.contains(":"))
                                Endtime=Endtime.substring(0,1);
                            if(Begintime.contains(":"))
                                Begintime=Begintime.substring(0,1);
                            int End_time=Integer.parseInt(Endtime);
                            int Begin_time=Integer.parseInt(Begintime);
                            if(End_time<7)
                                End_time+=12;
                            if(Begin_time<7)
                                Begin_time+=12;
                            RemainingHours[i]=RemainingHours[i]+(End_time-Begin_time)+1;

                        }
                    }
                }


            } catch (JSONException e)

            {
                e.printStackTrace();
            }



        }
    }
}

