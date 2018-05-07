package com.example.minal.studentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hold1.pagertabsindicator.PagerTabsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeTable extends AppCompatActivity
{

    //Data firelds:

    //Tag Related Data
    private String TAG = "Response to Time table: ";

    //General Access To user Id and Password
    private String ID = LoginActivity.username;
    private String Password = LoginActivity.password;

    //TimeTable Service invocation
    private String Timetable_invoke = ID+","+Password+",1";

    //Soap Resulting Data Streams:
    private SoapPrimitive resultString;
    private String data = null;
    private String SingleParsed_CourseName = "";
    private String SingleParsed_Location = "";
    private String SingleParsed_From = "";
    private String SingleParsed_To = "";
    private String SingleParsed_Type = "";

    //Local Data For Utility Usage and Layout Settings--> Class Variables:
    private String[] Days = { LoginActivity.username +"_"+"Saturday",LoginActivity.username +"_"+"Sunday",LoginActivity.username +"_"+"Monday",LoginActivity.username +"_"+"Tuesday",LoginActivity.username +"_"+"Wednesday",LoginActivity.username +"_"+"Thursday"};
    private ViewPager viewPager;
    private PagerTabsIndicator tabsIndicator;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView recyclerView;
    private List<TimeSlot> cartList;
    private timetable_Adapter mAdapter;
    private String CurrentSemester = "Current Semester";
    private android.support.v7.widget.Toolbar timetableToolbar;

    public TimeTable() //Default Constructor
    {
        cartList = new ArrayList<>();
    }

    //Class for backgrond operations:
    // used to get the Data from online service and parse it to the Layout
    public class AsyncCallWS_ReadTimeTable extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            Log.i(TAG, "onPreExecute");

            viewPager = (ViewPager) findViewById(R.id.view_page);
            tabsIndicator = (PagerTabsIndicator) findViewById(R.id.tabs_indicator);
            viewPager.setAdapter(new CustomViewPage_Adapter(TimeTable.this,cartList));
            tabsIndicator.setViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {
                    //fetchRecipes(i);
                }
                @Override
                public void onPageSelected( int i) {
                    tabsIndicator.setVerticalScrollbarPosition(i);
                    tabsIndicator.setHighlightText(true);

                    fetchRecipes(i);
                }
                @Override
                public void onPageScrollStateChanged(int i) {
                    // Left Empty to disable Scrolling

                }
            });

            progressDialog= new ProgressDialog(TimeTable.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.i(TAG, "doInBackground");

            if(new ConnectionDetector(TimeTable.this).isConnected() && cartList.size()==0)
            {
                try
                {
                    Get_TimeTable(); //Thread May Throw many exceptions
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Invoke_Toast("Toast is Invoked in doBackGround--> TimeTable",TimeTable.this);
                }
            }
            {
                progressDialog.setMessage("Almost Completed...");
                //Try To Read TimeTable in Offline mode
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                if(day == 7 || day==6) day = 0;
                Read_Timetable_Offline timetable_offline = new Read_Timetable_Offline(TimeTable.this,cartList,Days[day]);
            }
            mAdapter = new timetable_Adapter(TimeTable.this, cartList); //Seif: change hre
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.i(TAG, "onPostExecute");
            recyclerView.setAdapter(mAdapter);
            timetableToolbar.setTitle(CurrentSemester);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mShimmerViewContainer.stopShimmerAnimation();
            recyclerView.addItemDecoration(new MyDividerItemDecoration(TimeTable.this, LinearLayoutManager.VERTICAL, 16));

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if(day == 7 || day==6) day = 0;
            viewPager.setCurrentItem(day);
            fetchRecipes(day);
            progressDialog.cancel();
        }

        //service data is parsed into time table slots, each slot is added independently to the layout with its owwn data
        private void Get_TimeTable()
        {
            SOAP_Access serverAccessClass= SOAP_Access._getInstance();
            resultString = serverAccessClass.getResponse(Timetable_invoke);

            try
            {
                data = resultString.toString();

                JSONObject JBO_AllData = new JSONObject(data);
                JSONObject TimeTableObject = (JSONObject) JBO_AllData.get("Timetable");
                JSONArray DayObject = (JSONArray) TimeTableObject.get("Day");

                JSONObject DetailsObject = (JSONObject) TimeTableObject.get("Details");

                CurrentSemester = DetailsObject.getString("Semester_Name");
                JSONObject DayData = null;
                JSONArray DayDataEntry = null;
                timetable_Write newDay = null;
                for (int iterator = 0; iterator < DayObject.length(); iterator++)
                {
                    DayData = (JSONObject) DayObject.get(iterator);
                    newDay = new timetable_Write(getApplicationContext(),LoginActivity.username +"_"+DayData.get("Day_Name").toString());
                    DayDataEntry = (JSONArray) DayData.get("Entry");

                    for(int iterator2 = 0; iterator2 < DayDataEntry.length(); iterator2++)
                    {
                        JSONObject DataInstance_SubjectData = (JSONObject) DayDataEntry.get(iterator2);
                        SingleParsed_CourseName = DataInstance_SubjectData.get("Course_Name").toString();
                        SingleParsed_Location = DataInstance_SubjectData.get("Location").toString();
                        SingleParsed_Type = DataInstance_SubjectData.get("Type") + "";
                        SingleParsed_From = DataInstance_SubjectData.get("From") + "";
                        SingleParsed_To = DataInstance_SubjectData.get("To") + "";

                        newDay.addTitle(SingleParsed_CourseName);
                        newDay.addLocation(SingleParsed_Location);
                        newDay.addType(SingleParsed_Type);
                        newDay.addTimeFrom(SingleParsed_From);
                        newDay.addTimeTo(SingleParsed_To);

                        //for each day
                    }
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // On Destruction, Get back to Parent Activity
        Intent IntentActivity2 = new Intent(this,NavDrawerActivity.class);
        startActivity(IntentActivity2);

    }

    //Binding Shimmer View with its data
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        //initiating shimmer viewer:
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recycler_view_2);

        timetableToolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar2);
        timetableToolbar.setTitleTextColor(Color.WHITE);

        try {
            AsyncCallWS_ReadTimeTable gradesReader = new AsyncCallWS_ReadTimeTable();
            gradesReader.execute();
        } catch (Exception e) {
            Invoke_Toast("No Network Connection\nReading in Offline Mode", this); //If this fails, nothing is viewed by the user,
            return;
        }

        if(mAdapter!=null)
        {
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
       super.onPause();
    }

    private void fetchRecipes( int i)
    {
        //Data Fetching offline mode to user
        try
        {
            // Shoud be replaced by :
            if( this.cartList.isEmpty() == false ) this.cartList.clear(); //Flush Current Layout

            Read_Timetable_Offline offline_TimeTable = new Read_Timetable_Offline(getApplicationContext(), this.cartList, Days[i]);

            News_Site news_site = new News_Site(TimeTable.this, LoginActivity.username+"Course_Name");
            CurrentSemester =news_site.readSavedData();
            CurrentSemester = CurrentSemester.substring(0,CurrentSemester.length()-1); //Remove newLine Command
            timetableToolbar.setTitle(CurrentSemester);

            // refreshing recycler view
            mAdapter.notifyDataSetChanged();
            // stop animating Shimmer and hide the layout
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            /*Toast couldnot = Toast.makeText(getApplicationContext(),"Could not read Time Table in Offline Mode\nPlease Connect to internet to refresh data", Toast.LENGTH_LONG);
            couldnot.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            couldnot.show();*/
        }

    }

    private void Invoke_Toast(String ToastMessage, Context ctx)
    {
        Toast Error_CreatingDeadline = Toast.makeText(ctx, ToastMessage, Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }
}
