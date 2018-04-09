package com.example.minal.studentapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        AsyncCallWS_ReadAttendance gradesReader = new AsyncCallWS_ReadAttendance();
        gradesReader.execute();

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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            TextView textView_SubjectName = (TextView) findViewById(R.id.text3);
            textView_SubjectName.setText(dataParsed_SubjectName);
            TextView textView_Day = (TextView) findViewById(R.id.text1);
            textView_Day.setText(dataParsed_Day);
            TextView textView_Week = (TextView) findViewById(R.id.text2);
            textView_Week.setText(dataParsed_Week);
            TextView textView_Session = (TextView) findViewById(R.id.text4);
            textView_Session.setText(dataParsed_Session);
        }


        private void Get_Attendance() {

            SOAP_Access serverAccessClass= SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Attendance_invoke);

            try{
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
                    dataParsed_Week = dataParsed_Week + SingleParsed_Week+ "\n\n";
                    dataParsed_Session = dataParsed_Session + SingleParsed_Session+ "\n\n";

                    if(SingleParsed_SubjectName.length()>18)
                    {
                        dataParsed_Day+="\n";
                        dataParsed_Week+="\n";
                        dataParsed_Session+="\n";
                    }
                }



            } catch(JSONException e)

            {
                e.printStackTrace();
            }


        }

    }

}

