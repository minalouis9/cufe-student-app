package com.example.minal.studentapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class GPACalculator extends AppCompatActivity {

    private String TAG = "Response to Class Grades: ";
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String GPATranscript_invoke = ID+","+Password+",2";
    private SoapPrimitive resultString;

    private String data = null;
    private String dataParsed_SemesterName = "";
    private String SingleParsed_SemesterName = "";
    private String dataParsed_Cumulative = "";
    private String dataParsed_CreditHrs = "";

    public static String GPA_Json;
    public static String[] Semesters= new String[50];
    public static String SemesterName;
    public static int Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpacalculator);
                AsyncCallWS_ReadSemesters semesterReader = new AsyncCallWS_ReadSemesters();
                semesterReader.execute();

    }



    protected void Add_buttons() {

        final RelativeLayout lm = (RelativeLayout) findViewById(R.id.LO);

        // create the layout params that will be used to define how your
        // button will be displayed

        int top=0;
        String Semester;
        for (int j = 0; j < Count; j++) {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.width=1500;
            params.topMargin=top;
            top+=130;
            // Create LinearLayout
            RelativeLayout ll = new RelativeLayout(this);

            // Create Button
            final Button btn = new Button(this);
            // Give button an ID
            btn.setId(j + 1);
            btn.setText(Semesters[j]);
            // set the layoutParams on the button
            btn.setLayoutParams(params);
            btn.bringToFront();
            // Set click listener for button

            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Send = (String) btn.getText();
                    OpenSemesterGPA(Send);
                }
            });
            //Add button to LinearLayout
            ll.addView(btn);
            //Add button to RelativeLayout defined in XML
            lm.addView(ll);
        }
    }

    private void OpenSemesterGPA(String s)
    {
        Intent To_Semester = new Intent(this, SemesterCalculator.class);
        startActivity(To_Semester);
        SemesterName=s;
    }

    private class AsyncCallWS_ReadSemesters extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            Get_GPA();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //TextView textView_SubjectName = (TextView) findViewById(R.id.text1);
            //textView_SubjectName.setText(dataParsed_SemesterName);
            TextView textView_CumulativeGPA = (TextView) findViewById(R.id.textView2);
            textView_CumulativeGPA.setText(dataParsed_Cumulative);
            TextView textView_CreditHrs = (TextView) findViewById(R.id.textView4);
            textView_CreditHrs.setText(dataParsed_CreditHrs);
            Add_buttons();


        }
    }

    private void SaveData(String jsonData)
    {
        try {
            FileOutputStream gpa_File = GPACalculator.this.openFileOutput(ID+"GPA", GPACalculator.this.MODE_PRIVATE);
            gpa_File.write(jsonData.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private String ReadOfflineData()
    {
        String DataOut = "",bufferedLine="";
        FileInputStream ReadFile = null;
        try {
            ReadFile = GPACalculator.this.openFileInput(ID+"GPA");
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            while ((bufferedLine = Readings_Buffer.readLine()) != null)
            {
                DataOut += bufferedLine+= '\n';
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DataOut;
    }
        private void Get_GPA() {

            if((new ConnectionDetector(GPACalculator.this)).isConnected()==false) {

                data = ReadOfflineData();
            }
            else {
                SOAP_Access serverAccessClass = SOAP_Access._getInstance();

                resultString = serverAccessClass.getResponse(GPATranscript_invoke);
                SaveData(resultString.toString());
                data = resultString.toString();
            }

            try{


                if(data.length()==0) {
                    return;
                }
                GPA_Json = data;
                JSONObject JBO_AllData = new JSONObject(data);
                JSONObject Transcript = (JSONObject)JBO_AllData.get("GPA_Transcript");
                JSONArray Semeters = (JSONArray) Transcript.get("Semester");
                JSONObject details = (JSONObject) Transcript.get("Details");

                Count=0;
                for (int iterator = 0; iterator < Semeters.length(); iterator++) {
                    JSONObject DataInstance_SubjectData = (JSONObject) Semeters.get(iterator);

                    SingleParsed_SemesterName = DataInstance_SubjectData.get("Semester_Name") + "";
                    dataParsed_SemesterName = dataParsed_SemesterName + SingleParsed_SemesterName + "\n";
                    Semesters[iterator]= DataInstance_SubjectData.get("Semester_Name") + "";
                    Count++;


                }
                dataParsed_Cumulative = details.get("Student_GPA")+"";
                dataParsed_CreditHrs = details.get("Student_Total_Credits")+"";



            } catch(JSONException e)

            {
                e.printStackTrace();
            }


        }

    }

