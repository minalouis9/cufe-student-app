package com.example.minal.studentapp;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;

public class Semester_TermResult extends AppCompatActivity {
    private String TAG = "Response to Class Grades: ";
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Term_Classwork_invoke = ID+","+Password+",";
    private SoapPrimitive resultString;

    private String data = null;
    private String SingleParsed_CourseName;
    private String SingleParsed_Total_Max;
    private String SingleParsed_Term1 ;
    private String SingleParsed_Term1_Max;
    private String SingleParsed_Term2 ;
    private String SingleParsed_Term2_Max;
    private String SingleParsed_Total;
    public static int TermOrCoursework=1;


    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView recyclerView;
    private List<Semester_GradeTerm> cartList;
    private SemesterTermResults_Adapter mAdapter;
    android.support.v7.widget.Toolbar timetableToolbar;
    TextView student_name;
    TextView StudentTotalGrade;

    private String StudentName;

    private String Total_Grad_Student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester__term_result);
        cartList = new ArrayList<>();

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recycler_view_2);

        student_name = (TextView)findViewById(R.id.StudentName_Sem);
        StudentTotalGrade = (TextView)findViewById(R.id.total_Grade_Sem);

        AsyncCallWS_SemReadGrades asyncCallWS_semReadGrades = new AsyncCallWS_SemReadGrades();
        asyncCallWS_semReadGrades.execute();



        if(mAdapter!=null)
        {
            mAdapter.notifyDataSetChanged();
        }


    }


    private class AsyncCallWS_SemReadGrades extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");


        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            if(new ConnectionDetector(Semester_TermResult.this).isConnected() && cartList.size()==0) {

                try {
                    Get_Grades(); //Thread May Throw many exceptions

                    //Go to today's time table
                    mAdapter = new SemesterTermResults_Adapter(Semester_TermResult.this, cartList); //Seif: change hre
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            recyclerView.setAdapter(mAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mShimmerViewContainer.stopShimmerAnimation();
            recyclerView.addItemDecoration(new MyDividerItemDecoration(Semester_TermResult.this, LinearLayoutManager.VERTICAL, 16));

            student_name .setText(StudentName);
            StudentTotalGrade.setText(Total_Grad_Student);

            mAdapter.notifyDataSetChanged();
            // stop animating Shimmer and hide the layout
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
        }


        private void Get_Grades() {

            SOAP_Access serverAccessClass= SOAP_Access._getInstance();

            String DataParsingKey="";
            if(TermOrCoursework == 1)
            {
                DataParsingKey = "Term_Results";
                resultString = serverAccessClass.getResponse(Term_Classwork_invoke+"11");

            }
            else
            {
                resultString = serverAccessClass.getResponse(Term_Classwork_invoke+"12");

                DataParsingKey = "Classwork";
            }



            try{
                data = resultString.toString();

                //7aga keda temporaritly

                   // data += "}";

                JSONObject JBO_AllData = new JSONObject(data);


                JSONObject TermResultObject = (JSONObject) JBO_AllData.get(DataParsingKey);
                JSONArray Data_TermClasswork = (JSONArray) TermResultObject.get("Course");

                Semester_GradeTerm semester_gradeTerm;

                for (int iterator = 0; iterator < Data_TermClasswork.length(); iterator++) {

                    JSONObject DataInstance_SubjectData = (JSONObject) Data_TermClasswork.get(iterator);
                    SingleParsed_CourseName = DataInstance_SubjectData.get("Course_Name") + "";
                    SingleParsed_Term1 = DataInstance_SubjectData.get("Term_1_Grade") + "";
                    SingleParsed_Term1_Max = DataInstance_SubjectData.get("Term_1_Max_Grade") + "";
                    SingleParsed_Term2 = DataInstance_SubjectData.get("Term_2_Grade") + "";
                    SingleParsed_Term2_Max = DataInstance_SubjectData.get("Term_2_Max_Grade") + "";
                    SingleParsed_Total = DataInstance_SubjectData.get("Total_Grade") + "";
                    SingleParsed_Total_Max = DataInstance_SubjectData.get("Total_Max_Grade") + "";

                    if(SingleParsed_CourseName.compareTo("")==0) SingleParsed_CourseName = "Unknown";
                    if(SingleParsed_Term1.compareTo("")==0) SingleParsed_Term1 = "_";
                    if(SingleParsed_Term1_Max.compareTo("")==0) SingleParsed_Term1_Max = "_";
                    if(SingleParsed_Term2.compareTo("")==0) SingleParsed_Term2 = "_";
                    if(SingleParsed_Term2_Max.compareTo("")==0) SingleParsed_Term2_Max = "_";
                    if(SingleParsed_Total.compareTo("")==0) SingleParsed_Total = "_";
                    if(SingleParsed_Total_Max.compareTo("")==0) SingleParsed_Total_Max = "_";


                    semester_gradeTerm = new Semester_GradeTerm ();
                    semester_gradeTerm.setCourseName(SingleParsed_CourseName);
                    semester_gradeTerm.setTerm1_Grade(SingleParsed_Term1);
                    semester_gradeTerm.setTerm1_Grade_Max(SingleParsed_Term1_Max);
                    semester_gradeTerm.setTerm2_Grade(SingleParsed_Term2);
                    semester_gradeTerm.setTerm2_Grade_Max(SingleParsed_Term2_Max);
                    semester_gradeTerm.setTotalGrade(SingleParsed_Total);
                    semester_gradeTerm.setTotalGradeMax(SingleParsed_Total_Max);
                    cartList.add(semester_gradeTerm);
                }

                if(TermOrCoursework == 1) {
                    JSONObject studentInstance = (JSONObject) TermResultObject.get("Total_Grade");
                    JSONObject studentDetails = (JSONObject) TermResultObject.get("Student_Details");

                    StudentName = "Student Name: "+studentDetails.get("Student_Name") + "";
                    Total_Grad_Student = "Total Grade: " + studentInstance.get("Grade_Average") + ", " + studentInstance.get("Total_Grade") + "";
                }
                else
                {
                    StudentName = "\tCourse Work";
                    Total_Grad_Student="";
                }

            } catch(JSONException e)

            {
                e.printStackTrace();
            }


        }

    }


}
