package com.example.minal.studentapp;

import android.graphics.Color;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Warning extends AppCompatActivity {
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Warning_invoke = ID+","+Password+",8";
    private SoapPrimitive resultString;
    private String data,Warnings,Name="";
    TextView textview_warn;
    EditText EditText_Title;
    private boolean noWarning;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        AsyncCallWS_ReadWarnings Reader = new AsyncCallWS_ReadWarnings();
        Reader.execute();
        textview_warn=(TextView) findViewById(R.id.textView1);
        EditText_Title = (EditText)findViewById(R.id.textView2);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        noWarning=false;
        toolbar.setTitleTextColor(Color.WHITE);
    }
    private class AsyncCallWS_ReadWarnings extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Get_Warning();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(noWarning){
                toolbar.setTitle("Warnings");
                textview_warn.setText("No Warnings");
                textview_warn.setTextColor(Color.BLACK);
            }
            else{
                toolbar.setBackgroundColor(Color.RED);
                toolbar.setTitle("Warnings");}
            if(!noWarning) {
                String FullString = "";
                int lastIndexOfBr = Warnings.lastIndexOf('>'),PrevLastIndex = Warnings.length();
                while(lastIndexOfBr != -1 && Warnings.substring(PrevLastIndex-5,PrevLastIndex).contains("<br>")==false)
                {
                    FullString = FullString+"\n> "+ Warnings.substring(lastIndexOfBr+2,PrevLastIndex);
                    PrevLastIndex = lastIndexOfBr-3;
                    lastIndexOfBr = Warnings.substring(0,PrevLastIndex).lastIndexOf('>');

                }
                String title = "";
                int a = Warnings.lastIndexOf("'center'");
                int z = Warnings.lastIndexOf("<br> <br>");
                title= Warnings.substring(a + 9, z - 1);

                textview_warn.setText(FullString);
                textview_warn.setTextColor(Color.BLACK);
                EditText_Title.setText(title);
                EditText_Title.setTextColor(Color.BLACK);
            }


        }


        private void Get_Warning() {
            if(ID.charAt(0)!='1')
                Warning_invoke= ID+","+Password+",15";
            SOAP_Access serverAccessClass = SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Warning_invoke);

            try {
                data = resultString.toString();
                JSONObject JBO_AllData = new JSONObject(data);
                JSONObject WarningObject = (JSONObject) JBO_AllData.get("Warnings");
                Warnings = WarningObject.get("Warning_String") + "";
                if(!(Warnings.contains("20"))) {
                    noWarning=true;
                }


            } catch (JSONException e)

            {
                e.printStackTrace();
            }



        }
    }

}

