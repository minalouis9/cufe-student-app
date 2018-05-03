package com.example.minal.studentapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Profile extends AppCompatActivity {
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Profile_invoke = ID+","+Password+",3";
    private SoapPrimitive resultString;

    private String data = null;
    private String ID_Parsed = "";
    private String Name_Parsed = "";
    private String Program_Parsed = "";
    private String GPA_Parsed = "";
    private String Total_credits_Parsed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        Profile.AsyncCallWS_ReadProfile ProfileReader = new Profile.AsyncCallWS_ReadProfile();
        ProfileReader.execute();
    }

    private class AsyncCallWS_ReadProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Get_Profile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            TextView textView_Name = (TextView) findViewById(R.id.textView4);
            textView_Name.setText(Name_Parsed);
            TextView textView_ID = (TextView) findViewById(R.id.textView2);
            textView_ID.setText(ID_Parsed);
            TextView textView_Program = (TextView) findViewById(R.id.textView6);
            textView_Program.setText(Program_Parsed);
            TextView textView_GPA = (TextView) findViewById(R.id.textView8);
            textView_GPA.setText(GPA_Parsed);
            TextView textView_Total_credits= (TextView) findViewById(R.id.textView10);
            textView_Total_credits.setText(Total_credits_Parsed);
            if(ID.charAt(0)!='1')
            {
                 TextView textView_Student_Address= (TextView) findViewById(R.id.textView7);
                 textView_Student_Address.setText("Student Address:");
                 TextView textView_Email= (TextView) findViewById(R.id.textView9);
                 textView_Email.setText("Email");
            }


        }


        private void Get_Profile() {
            if(ID.charAt(0)!='1')
                Profile_invoke= ID+","+Password+",14";

            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Profile_invoke);

            try {
                data = resultString.toString();
                JSONObject JBO_AllData = new JSONObject(data);
                JSONObject DataInstance_Profile = (JSONObject) JBO_AllData.get("Profile_Info");
                ID_Parsed = DataInstance_Profile.get("Student_Code") + "";
                Name_Parsed = DataInstance_Profile.get("Student_Name_EN") + "";
                if(ID.charAt(0)=='1') {
                    Program_Parsed = DataInstance_Profile.get("Student_Program_Name") + "";
                    GPA_Parsed = DataInstance_Profile.get("Student_GPA") + "";
                    Total_credits_Parsed = DataInstance_Profile.get("Student_Total_Credits") + "";
                } else {
                    Program_Parsed = DataInstance_Profile.get("Student_Department_Code") + "";
                    GPA_Parsed = DataInstance_Profile.get("Student_Address") + "";
                    Total_credits_Parsed = DataInstance_Profile.get("Student_Email") + "";
                }

            } catch (JSONException e)

            {
                e.printStackTrace();
            }


        }

    }

}
