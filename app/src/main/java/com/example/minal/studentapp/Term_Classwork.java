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



public class Term_Classwork extends AppCompatActivity {

    private String TAG = "Response to Class Grades: ";
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Term_Classwork_invoke = ID+","+Password+",5";
    private SoapPrimitive resultString;

    private String data = null;
    private String dataParsed_SubjectName = "";
    private String SingleParsed_SubjectName []=null;
    private String dataParsed_MidtermGrade = "";
    private String SingleParsed_MidtermGrade = "";
    private String dataParsed_DailyWorkGrade = "";
    private String SingleParsed_DailyWorkGrade = "";
    TextView textView_SubjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term__classwork);
        SingleParsed_SubjectName=new String[10];

        AsyncCallWS_ReadGrades gradesReader = new AsyncCallWS_ReadGrades();
        gradesReader.execute();

    }

    private class AsyncCallWS_ReadGrades extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            Get_Grades();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");

            TextView textView_MidtermGrade = (TextView) findViewById(R.id.text2);
            textView_MidtermGrade.setText(dataParsed_MidtermGrade);
            TextView textView_DailyWorkGrade = (TextView) findViewById(R.id.text3);
            textView_DailyWorkGrade.setText(dataParsed_DailyWorkGrade);
            int i=0;
            while(SingleParsed_SubjectName[i]!=null)
            {
                if (i == 0)
                    textView_SubjectName = (TextView) findViewById(R.id.text1);

                if(i==1)
                    textView_SubjectName = (TextView) findViewById(R.id.text4);
                if(i==2)
                    textView_SubjectName = (TextView) findViewById(R.id.text5);
                if(i==3)
                    textView_SubjectName = (TextView) findViewById(R.id.text6);
                if(i==4)
                    textView_SubjectName = (TextView) findViewById(R.id.text7);
                if(i==5)
                    textView_SubjectName = (TextView) findViewById(R.id.text8);
                if(i==6)
                    textView_SubjectName = (TextView) findViewById(R.id.text9);
                if(i==7)
                    textView_SubjectName = (TextView) findViewById(R.id.text10);
                if(i==8)
                    textView_SubjectName = (TextView) findViewById(R.id.text11);
                if(i==9)
                    textView_SubjectName = (TextView) findViewById(R.id.text12);

                SingleParsed_SubjectName[i]+="\n";

                textView_SubjectName.setText(SingleParsed_SubjectName[i]);
                textView_SubjectName.setMaxLines(2);
                i++;

            }




        }


        private void Get_Grades() {

            SOAP_Access serverAccessClass= SOAP_Access._getInstance();

            resultString = serverAccessClass.getResponse(Term_Classwork_invoke);

            try{
                data = resultString.toString();
                JSONObject JBO_AllData = new JSONObject(data);
                JSONArray Data_TermClasswork = (JSONArray) JBO_AllData.get("Term_Classwork");
            for (int iterator = 0; iterator < Data_TermClasswork.length(); iterator++) {
                JSONObject DataInstance_SubjectData = (JSONObject) Data_TermClasswork.get(iterator);
                SingleParsed_SubjectName[iterator] = DataInstance_SubjectData.get("Subject_Name") + "";
                SingleParsed_MidtermGrade = DataInstance_SubjectData.get("Midterm") + "";
                SingleParsed_DailyWorkGrade = DataInstance_SubjectData.get("DailyWork") + "";

                dataParsed_MidtermGrade = dataParsed_MidtermGrade + SingleParsed_MidtermGrade + "\n\n";
                dataParsed_DailyWorkGrade = dataParsed_DailyWorkGrade + SingleParsed_DailyWorkGrade + "\n\n";
                

            }



        } catch(JSONException e)

        {
            e.printStackTrace();
        }


    }

    }

}
