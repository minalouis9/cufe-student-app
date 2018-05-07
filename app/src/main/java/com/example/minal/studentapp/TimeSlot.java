package com.example.minal.studentapp;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmed on 4/13/2018.
 */

public class TimeSlot {

    private String TimeFrom, TimeTo, Title,Type,Location;
    private static final String TAG = "TimeSlot_Instantiation";

    public TimeSlot()
    {

    }

    public TimeSlot(String timeFrom, String timeTo, String title, String type, String location) {
        TimeFrom = timeFrom;
        TimeTo = timeTo;
        Title = title;
        Type = type;
        Location = location;
    }

    public String getTimeFrom() {
        return TimeFrom;
    }

    public String getTimeTo() {
        return TimeTo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTimeFrom(String timeFrom) {
        TimeFrom = timeFrom;

        int indexOfColon = TimeFrom.indexOf(':',0);
        int time1 = Integer.parseInt( TimeFrom.substring(0,indexOfColon));
        if(time1 >7 && time1 <12) TimeFrom += " AM ";
        else
        {
            TimeFrom += " PM ";
        }
    }

    public void setTimeTo(String timeTo) {
        TimeTo = timeTo;
        int indexOfColon = TimeTo.indexOf(':',0);
        int time1 = Integer.parseInt( TimeTo.substring(0,indexOfColon));
        if(time1 >7 && time1 <12) TimeTo += " AM ";
        else
        {
            TimeTo += " PM ";
        }
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getType() {
        return Type;
    }

    public String getLocation() {
        return Location;
    }


}
