package com.example.minal.studentapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class semester_transcript extends AppCompatActivity {

    private String TAG = "Response to Class Grades: ";

    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;
    private String Term_Classwork_invoke = ID+","+Password+",";
    private SoapPrimitive resultString;
    private String data = null;
    private String SingleParsed_Total_Max;
    private String SingleParsed_Term1 ;
    private String SingleParsed_Term1_Max;
    private String SingleParsed_Term2 ;
    private String SingleParsed_Term2_Max;
    private String SingleParsed_Total;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView recyclerView;
    private List<Semester_GradeTerm> cartList;
    private semestertranscript1_Adapter mAdapter;
    private String SingleParsed_CourseName;
        List<String> yearsNames;

    Map<String ,Year> dataMaps = new HashMap<String, Year>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_transcript);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recycler_view_2);

        AsyncCallWS_semester_transcript caller = new AsyncCallWS_semester_transcript();
        caller.execute();

    }


    private class AsyncCallWS_semester_transcript extends AsyncTask<Void, Void, Void> {


        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            cartList = new ArrayList<>();
            yearsNames = new ArrayList<>();

            progressDialog= new ProgressDialog(semester_transcript.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            if(new ConnectionDetector(semester_transcript.this).isConnected() && cartList.size()==0) {

                try {
                    Get_Grades(); //Thread May Throw many exceptions

                    //Go to today's time table
                    mAdapter = new semestertranscript1_Adapter(semester_transcript.this, yearsNames,dataMaps); //Seif: change hre
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
            recyclerView.addItemDecoration(new MyDividerItemDecoration(semester_transcript.this, LinearLayoutManager.VERTICAL, 16));

            mAdapter.notifyDataSetChanged();
            // stop animating Shimmer and hide the layout
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            progressDialog.cancel();
        }


        private void Get_Grades()
        {

            SOAP_Access serverAccessClass= SOAP_Access._getInstance();

            String DataParsingKey = "Student_Transcript";
                resultString = serverAccessClass.getResponse(Term_Classwork_invoke+"13");

            try{
                data = resultString.toString();

                JSONObject JBO_AllData = new JSONObject(data);

                JSONObject TermResultObject = (JSONObject) JBO_AllData.get(DataParsingKey);
                JSONArray Data_Years = (JSONArray) TermResultObject.get("Year");

                Semester_GradeTerm semester_gradeTerm;
                JSONObject Data_yearDetails = null;
                JSONArray Data_YearCourses= null;

                String YearName;

               for(int iterator2=0 ; iterator2<Data_Years.length();++iterator2)
                    {
                        JSONObject DataInstance_SubjectData = (JSONObject) Data_Years.get(iterator2);

                        YearName = DataInstance_SubjectData.get("Year_Name") + "";
                        yearsNames.add(YearName);


                        Data_YearCourses = (JSONArray) DataInstance_SubjectData.get("Course");
                        cartList = new ArrayList<>();
                        for (int iterator3 = 0; iterator3 < Data_YearCourses.length(); ++iterator3)

                        {
                            DataInstance_SubjectData = (JSONObject) Data_YearCourses.get(iterator3);
                            SingleParsed_CourseName = DataInstance_SubjectData.get("Course_Name") + "";
                            SingleParsed_Term1 = DataInstance_SubjectData.get("Term_1_Grade") + "";
                            SingleParsed_Term1_Max = DataInstance_SubjectData.get("Term_1_Max_Grade") + "";
                            SingleParsed_Term2 = DataInstance_SubjectData.get("Term_2_Grade") + "";
                            SingleParsed_Term2_Max = DataInstance_SubjectData.get("Term_2_Max_Grade") + "";
                            SingleParsed_Total = DataInstance_SubjectData.get("Total_Grade") + "";
                            SingleParsed_Total_Max = DataInstance_SubjectData.get("Total_Max_Grade") + "";

                            semester_gradeTerm = new Semester_GradeTerm();
                            semester_gradeTerm.setCourseName(SingleParsed_CourseName);
                            semester_gradeTerm.setTerm1_Grade(SingleParsed_Term1);
                            semester_gradeTerm.setTerm1_Grade_Max(SingleParsed_Term1_Max);
                            semester_gradeTerm.setTerm2_Grade(SingleParsed_Term2);
                            semester_gradeTerm.setTerm2_Grade_Max(SingleParsed_Term2_Max);
                            semester_gradeTerm.setTotalGrade(SingleParsed_Total);
                            semester_gradeTerm.setTotalGradeMax(SingleParsed_Total_Max);
                            cartList.add(semester_gradeTerm);
                        }
                        //NOW THIS YEAR HAS ALL ITS DATA, ADD IT TO LIST
                        dataMaps.put(YearName,new Year(cartList));
                    }

            } catch(JSONException e)

            {
                e.printStackTrace();
            }
        }
    }

}
