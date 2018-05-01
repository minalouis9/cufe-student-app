package com.example.minal.studentapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class SemesterTranscipt extends AppCompatActivity {

    private String SemesterNameInJson=FullTranscript.SemesterName;
    private int CountInJson=FullTranscript.Count;
    private String[] SemestersInJson= FullTranscript.Semesters;
    private String GPAInJson=FullTranscript.GPA_Json;
    private String[] StringGrades= new String[10];
    private int[] Hrs= new int[10];
    private double[] Grades= new double[10];
    private int Count;
    private double GPA;
    private int TotalHrs;

    private String data = null;
    private String dataParsed_SubjectName = "";
    private String SingleParsed_SubjectName = "";
    private String dataParsed_Grade = "";
    private String SingleParsed_Grade = "";
    private String dataParsed_Hrs = "";
    private String SingleParsed_Hrs = "";
    private String dataParsed_SemesterName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_gp);
        Get_GPA();
        CalculateGPA();
        DecimalFormat df = new DecimalFormat("#.###");
        TextView textView_SubjectName = (TextView) findViewById(R.id.text1);
        textView_SubjectName.setText(dataParsed_SubjectName);
        TextView textView_MidtermGrade = (TextView) findViewById(R.id.text2);
        textView_MidtermGrade.setText(dataParsed_Grade);
        TextView textView_DailyWorkGrade = (TextView) findViewById(R.id.text3);
        textView_DailyWorkGrade.setText(dataParsed_Hrs);
        TextView textView_SemesterName = (TextView) findViewById(R.id.textView8);
        textView_SemesterName.setText(dataParsed_SemesterName);
        TextView textView_SemesterGPA = (TextView) findViewById(R.id.textView2);
        textView_SemesterGPA.setText(df.format(GPA));
        TextView textView_SemesterHrs = (TextView) findViewById(R.id.textView4);
        textView_SemesterHrs.setText(String.valueOf(TotalHrs));
    }





    private void CalculateGPA()
    {
        for(int i=0;i<Count;i++)
        {
            if (StringGrades[i].equals("A+")|| StringGrades[i].equals("A"))
                Grades[i] = 4.0;
            else if (StringGrades[i].equals("A-"))
                Grades[i] = 3.7;
            else if (StringGrades[i].equals("B+"))
                Grades[i] = 3.3;
            else if (StringGrades[i].equals("B"))
                Grades[i] = 3.0;
            else if (StringGrades[i].equals("B-"))
                Grades[i] = 2.7;
            else if (StringGrades[i].equals("C+"))
                Grades[i] = 2.3;
            else if (StringGrades[i].equals("C"))
                Grades[i] = 2.0;
            else if (StringGrades[i].equals("C-"))
                Grades[i] = 1.7;
            else if (StringGrades[i].equals("D+"))
                Grades[i] = 1.3;
            else if (StringGrades[i].equals("D"))
                Grades[i] = 1.0;
            else if (StringGrades[i].equals("D-"))
                Grades[i] = 0.7;
            else if (StringGrades[i].equals("F"))
                Grades[i] = 0.0;
        }
        double Mul;
        double TotalPoints=0;
        TotalHrs=0;

        for(int i=0;i<Count;i++)
        {
            Mul=Grades[i]*Hrs[i];
            TotalPoints+=Mul;
            TotalHrs+=Hrs[i];
        }
        GPA= TotalPoints/TotalHrs;
    }

    private void Get_GPA() {

        try{
            data = GPAInJson;
            JSONObject JBO_AllData = new JSONObject(data);
            JSONObject Transcript = (JSONObject)JBO_AllData.get("GPA_Transcript");
            JSONArray Semeters = (JSONArray) Transcript.get("Semester");
            int SemesterNoInJson=0;
            for(int i=0;i<CountInJson;i++)
            {
                if(SemestersInJson[i]==SemesterNameInJson)
                    SemesterNoInJson=i;
            }

            JSONObject Data_SubjectData = (JSONObject) Semeters.get(SemesterNoInJson);
            dataParsed_SemesterName= Data_SubjectData.get("Semester_Name") + "";
            JSONArray Subjects = (JSONArray) Data_SubjectData.get("Entry");

            Count=Subjects.length();

            for (int iterator = 0; iterator < Subjects.length(); iterator++) {
                JSONObject DataInstance_SubjectData = (JSONObject) Subjects.get(iterator);

                SingleParsed_SubjectName = DataInstance_SubjectData.get("Course_Name") + "";
                if(SingleParsed_SubjectName.length()>33)
                {
                    int n = 33;

                    while (SingleParsed_SubjectName.charAt(n) != " ".charAt(0))
                    {
                        n--;
                    }
                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName.substring(0, n)+ "\n";
                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName.substring(n+1)+ "\n";
                }
                else
                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName + "\n";

                SingleParsed_Grade = DataInstance_SubjectData.get("Grade") + "";
                dataParsed_Grade = dataParsed_Grade + SingleParsed_Grade + "\n";

                SingleParsed_Hrs = DataInstance_SubjectData.get("Hours") + "";
                dataParsed_Hrs = dataParsed_Hrs + SingleParsed_Hrs + "\n";

                if(SingleParsed_SubjectName.length()>33) {
                    dataParsed_Grade+="\n";
                    dataParsed_Hrs+="\n";
                }
                StringGrades[iterator]= SingleParsed_Grade;
                Hrs[iterator]= Integer.parseInt(SingleParsed_Hrs);
            }

        } catch(JSONException e)

        {
            e.printStackTrace();
        }
    }
}

