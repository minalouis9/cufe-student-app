package com.example.minal.studentapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Deadlines_Track extends AppCompatActivity {

    private ShimmerFrameLayout mShimmerViewContainer;

    private RecyclerView recyclerView;
    private List<Deadline> cartList;
    private deadlinelist_adapter mAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent IntentActivity2 = new Intent(this,NavDrawerActivity.class);
        startActivity(IntentActivity2);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deadlines__track);
/////////////////////////////

///////////////////////////////////////


        //initiating shimmer viewer:
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        recyclerView = findViewById(R.id.recycler_view);


        ReadDeadlines.LoadAllDeadlines(Deadlines_Track.this,LoginActivity.username);
        this.cartList = ReadDeadlines.DeadlinesList;
        mAdapter = new deadlinelist_adapter(this, cartList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        // making http call and fetching menu json

        fetchRecipes();
        //our reference Layout:
        FloatingActionButton Add_Deadline_Invoke = (FloatingActionButton) findViewById(R.id.NewDeadline_floatingActionButton);
        Add_Deadline_Invoke.setOnClickListener(
                new FloatingActionButton.OnClickListener() {
                    public void onClick(View v) {
                        Intent To_NewDeadlines = new Intent(getBaseContext(), NewDeadline.class);
                        startActivity(To_NewDeadlines);
                    }
                }
        );


        EditText No_DeadlinesYet = (EditText)findViewById(R.id.No_DeadlinesYet);

        if(this.cartList.isEmpty())
        {
            No_DeadlinesYet.setVisibility(View.VISIBLE);
        }
        else
        {
            No_DeadlinesYet.setVisibility(View.GONE);
        }

        SharedPreferences sharedpreferences = getSharedPreferences("Deadlines", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String[] Message2 = ReadDeadlines.getNoyification(Deadlines_Track.this);
        editor.putString("Deadlines_Notification_Message", Message2[0]);
        editor.putString("Deadlines_DueDate", Message2[1]);
        editor.putString("Deadlines_DueDate_Increment",Message2[2]);

        editor.commit();
    }
    public void ReadAllDeadlines(ReadDeadlines Reader)
    {


        if(Reader.isEmpty() == false) {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    private void fetchRecipes()
    {

        try{
            ReadDeadlines.LoadAllDeadlines(this,LoginActivity.username);
            this.cartList = ReadDeadlines.DeadlinesList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast couldnot = Toast.makeText(getApplicationContext(),"Could not read deadlines", Toast.LENGTH_LONG);
            couldnot.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            couldnot.show();
        }

        // refreshing recycler view
        mAdapter.notifyDataSetChanged();

        // stop animating Shimmer and hide the layout
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }


}

