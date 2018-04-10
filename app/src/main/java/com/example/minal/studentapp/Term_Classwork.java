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
    private String SingleParsed_SubjectName = "";
    private String dataParsed_MidtermGrade = "";
    private String SingleParsed_MidtermGrade = "";
    private String dataParsed_DailyWorkGrade = "";
    private String SingleParsed_DailyWorkGrade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term__classwork);

        AsyncCallWS_ReadGrades gradesReader = new AsyncCallWS_ReadGrades();
        gradesReader.execute();
    }

    private class AsyncCallWS_ReadGrades extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {Log.i(TAG, "onPreExecute");}

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.i(TAG, "doInBackground");

            Get_Grades();
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.i(TAG, "onPostExecute");

            TextView textView_SubjectName = (TextView) findViewById(R.id.text1);
            textView_SubjectName.setText(dataParsed_SubjectName);

            TextView textView_MidtermGrade = (TextView) findViewById(R.id.text2);
            textView_MidtermGrade.setText(dataParsed_MidtermGrade);

            TextView textView_DailyWorkGrade = (TextView) findViewById(R.id.text3);
            textView_DailyWorkGrade.setText(dataParsed_DailyWorkGrade);
        }

        private void Get_Grades()
        {
            SOAP_Access serverAccessClass= SOAP_Access._getInstance();
            resultString = serverAccessClass.getResponse(Term_Classwork_invoke);

            try
            {
                data = resultString.toString();

                JSONObject JBO_AllData = new JSONObject(data);
                JSONArray Data_TermClasswork = (JSONArray) JBO_AllData.get("Term_Classwork");

                for (int iterator = 0; iterator < Data_TermClasswork.length(); iterator++)
                {
                    JSONObject DataInstance_SubjectData = (JSONObject) Data_TermClasswork.get(iterator);
                    SingleParsed_SubjectName = DataInstance_SubjectData.get("Subject_Name") + "";
                    SingleParsed_MidtermGrade = DataInstance_SubjectData.get("Midterm") + "";
                    SingleParsed_DailyWorkGrade = DataInstance_SubjectData.get("DailyWork") + "";

                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName + "\n";
                    dataParsed_MidtermGrade = dataParsed_MidtermGrade + SingleParsed_MidtermGrade + "\n";
                    dataParsed_DailyWorkGrade = dataParsed_DailyWorkGrade + SingleParsed_DailyWorkGrade + "\n";
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
         }
    }
}
