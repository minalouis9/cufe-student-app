package com.example.minal.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
ConnectionDetector cdr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cdr=new ConnectionDetector(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        CardView Maps = findViewById(R.id.CampusmapCardId);
        Maps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                OpenMaps();
            }
        });

        CardView Term_classwork = findViewById(R.id.CourseworkCardId);
        Term_classwork.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cdr.isConnected())
                OpenTerm_classwork();
                 else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();
            }
        });

        CardView Attendance = findViewById(R.id.AttendanceCardId);
        Attendance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cdr.isConnected())
                OpenAttendance();
                else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();
            }
        });

        CardView News = findViewById(R.id.NewsCardId);
        News.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cdr.isConnected())
                    OpenNews();
                else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();
            }
        });

        CardView Statistic = findViewById(R.id.StatsticCardId);
        Statistic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cdr.isConnected())
                    OpenStatistics();
                else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();

            }
        });
/*
        CardView GPATranscript = findViewById(R.id.TranscriptCardId);
        GPATranscript.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(cdr.isConnected())
                    OpenGPA();
                else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();

            }
        });
*/
        CardView FullTranscript = findViewById(R.id.TranscriptCardId);
        FullTranscript.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                    OpenTranscript();
            }
        });

        CardView Deadlines = findViewById(R.id.DeadlinesCardId);
        Deadlines.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                OpenDeadline();
            }
        });

        CardView Timetable = findViewById(R.id.ScheduleCardId);
        Timetable.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v)
        {OpenTimetable();}
        });
    }

    private void LogOut()
    {
        Intent IntentLoginActivity = new Intent(this,LoginActivity.class);
        startActivity(IntentLoginActivity);
        finish();
    }

    private void OpenTimetable()
    {
        Intent To_Timetable = new Intent(this, TimeTable.class);
        startActivity(To_Timetable);
    }

    private void OpenTranscript()
    {
        Intent To_Transcript = new Intent(this, FullTranscript.class);
        startActivity(To_Transcript);
    }

    private void OpenGPA()
    {
        Intent To_Transcript = new Intent(this, GPATranscript.class);
        startActivity(To_Transcript);
    }

    private void OpenDeadline()
    {
        Intent To_Deadlines = new Intent(this, Deadlines_Track.class);
        startActivity(To_Deadlines);
    }

    public void OpenMaps()
    {
        Intent IntentMapsActivity = new Intent(this,MapsActivity.class);
        startActivity(IntentMapsActivity);
    }

    public void OpenAttendance()
    {
        Intent IntentAttendance = new Intent(this,Attendance.class);
        startActivity(IntentAttendance);
    }
    public void OpenProfile()
    {
        Intent IntentProfile = new Intent(this,Profile.class);
        startActivity(IntentProfile);
    }
    public void OpenStatistics()
    {
        Intent IntentStatistic = new Intent(this,Statistics.class);
        startActivity(IntentStatistic);
    }
    public void OpenWarnings()
    {
        Intent IntentWarning = new Intent(this,Warning.class);
        startActivity(IntentWarning);
    }
    public void OpenNews()
    {
        Intent IntentNews = new Intent(this,News.class);
        startActivity(IntentNews);
    }
    public void OpenTerm_classwork()
    {
        Intent IntentTerm_classworkActivity = new Intent(this,Term_Classwork.class);
        startActivity(IntentTerm_classworkActivity);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Home_Itm) {
            // Handle the camera action
        } else if (id == R.id.Profile_Itm) {
            if(cdr.isConnected())
                OpenProfile();
            else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();

        } else if (id == R.id.Warning_Itm) {
            if(cdr.isConnected())
                OpenWarnings();
            else  Toast.makeText(getBaseContext(), "Network Connection Failed", Toast.LENGTH_LONG).show();

        } else if (id == R.id.Exams_Itm) {

        } else if (id == R.id.Stats_Itm) {

        } else if (id == R.id.Logout_Itm) {
            // disable going back to the MainActivity
            moveTaskToBack(true);
            LoginActivity.loginPrefs_Editor.putBoolean("StayLogged",false);
            LoginActivity.loginPrefs_Editor.commit();
            LogOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}


