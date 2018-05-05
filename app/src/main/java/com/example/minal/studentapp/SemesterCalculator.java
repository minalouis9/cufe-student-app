package com.example.minal.studentapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.view.View;
import android.widget.ArrayAdapter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class SemesterCalculator extends AppCompatActivity {

    private String SemesterNameInJson=GPACalculator.SemesterName;
    private int CountInJson=GPACalculator.Count;
    private String[] SemestersInJson= GPACalculator.Semesters;
    private String GPAInJson=GPACalculator.GPA_Json;
    private String[] SubjectNames= new String[10];
    private String[] StringGrades= new String[10];
    private int[] CheckArrray= new int[10];
    private int[] Hrs= new int[10];
    private double[] Grades= new double[10];
    private int Count;
    private double GPA;
    private int TotalHrs;

    private String data = null;
    private String dataParsed_SubjectName = "";
    private String SingleParsed_SubjectName = "";
    private String SingleParsed_Grade = "";
    private String dataParsed_Hrs = "";
    private String SingleParsed_Hrs = "";
    private String dataParsed_SemesterName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_calculator);

        Get_GPA();
        CalculateGPA();
        Add_Spinner();
        //DecimalFormat df = new DecimalFormat("#.###");
        TextView textView_SubjectName = (TextView) findViewById(R.id.text1);
        textView_SubjectName.setText(dataParsed_SubjectName);
        TextView textView_DailyWorkGrade = (TextView) findViewById(R.id.text3);
        textView_DailyWorkGrade.setText(dataParsed_Hrs);
        TextView textView_SemesterName = (TextView) findViewById(R.id.textView8);
        textView_SemesterName.setText(dataParsed_SemesterName);
        TextView textView_SemesterHrs = (TextView) findViewById(R.id.textView4);
        textView_SemesterHrs.setText(String.valueOf(TotalHrs));
        SaveData(Integer.toString(TotalHrs)+"\n"+Double.toString(GPA));
    }

    protected void Add_Spinner() {

        final RelativeLayout lm = (RelativeLayout) findViewById(R.id.LO);

        // create the layout params that will be used to define how your
        // button will be displayed

        int top=-50;

        for (int j = 0; j < Count; j++) {

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.width=180;
            params.alignWithParent=true;
            params.leftMargin=780;
            params.topMargin=top;
            if(CheckArrray[j]==1)
                top+=150;
            else
                top+=100;
            // Create LinearLayout
            RelativeLayout ll = new RelativeLayout(this);

            // Create Button
            final Spinner Spin = new Spinner(this);
            // Give button an ID
            Spin.setId(j + 1);

            // set the layoutParams on the button
            Spin.setLayoutParams(params);
            Spin.bringToFront();
            // Set click listener for button



            //create a list of items for the spinner.
            String[] items = new String[]{"Gr","A+", "A", "A-","B+", "B", "B-","C+", "C", "C-","D+", "D", "D-","F"};
            //create an adapter to describe how the items are displayed, adapters are used in several places in android.
            //There are multiple variations of this, but this is the basic variant.
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            //set the spinners adapter to the previously created one.
            Spin.setAdapter(adapter);

            int spinnerPosition = adapter.getPosition(StringGrades[j]);
            Spin.setSelection(spinnerPosition);


            Spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String text = Spin.getSelectedItem().toString();
                    int x =Spin.getId();
                    StringGrades[x-1]=text;
                    CalculateGPA();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            ll.addView(Spin);

            lm.addView(ll);
        }
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
        DecimalFormat df = new DecimalFormat("#.###");
        TextView textView_SemesterGPA = (TextView) findViewById(R.id.textView2);
        textView_SemesterGPA.setText(df.format(GPA));
    }

    private void SaveData(String SmsesterData)
    {
        try {
            FileOutputStream gpa_File = SemesterCalculator.this.openFileOutput(dataParsed_SemesterName, SemesterCalculator.this.MODE_PRIVATE);
            gpa_File.write(SmsesterData.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName.substring(n+1)+ "\n"+"\n";
                }
                else
                    dataParsed_SubjectName = dataParsed_SubjectName + SingleParsed_SubjectName + "\n"+"\n";

                SingleParsed_Grade = DataInstance_SubjectData.get("Grade") + "";

                SingleParsed_Hrs = DataInstance_SubjectData.get("Hours") + "";
                dataParsed_Hrs = dataParsed_Hrs + SingleParsed_Hrs + "\n";

                if(SingleParsed_SubjectName.length()>33) {
                    CheckArrray[iterator]=1;
                    dataParsed_Hrs+="\n"+"\n";
                }
                else
                {
                    CheckArrray[iterator]=0;
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


