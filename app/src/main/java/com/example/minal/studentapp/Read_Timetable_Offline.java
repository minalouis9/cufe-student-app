package com.example.minal.studentapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ahmed on 4/15/2018.
 */

public class Read_Timetable_Offline {

    //Data fields:
    private List<TimeSlot> TimeslotList;
    private String TAG = "Reading timetable offine";

    public Read_Timetable_Offline(Context cntx, List<TimeSlot> deadlinesLiseIn, String DayLabel) {
        TimeslotList = deadlinesLiseIn;
        LoadTodaySlots(cntx, DayLabel);
    }

    private void LoadTodaySlots(Context cntx, String DayLabel) {

        FileInputStream ReadFile = null;
        TimeSlot nextTimeSlot = null;
        try {

            ReadFile = cntx.openFileInput(DayLabel);
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            String TimeFrom = null;
            while ((TimeFrom = Readings_Buffer.readLine()) != null) {

                nextTimeSlot = new TimeSlot();
                nextTimeSlot.setTitle(TimeFrom);
                Log.i(TAG, "Desc: " + nextTimeSlot.getTimeFrom());

                nextTimeSlot.setLocation(Readings_Buffer.readLine().toString());
                Log.i(TAG, "Label: " + nextTimeSlot.getTimeTo());

                nextTimeSlot.setType(Readings_Buffer.readLine().toString());
                Log.i(TAG, "Duedate: " + nextTimeSlot.getTitle());

                nextTimeSlot.setTimeFrom(Readings_Buffer.readLine().toString());
                Log.i(TAG, "Type: " + nextTimeSlot.getType());

                nextTimeSlot.setTimeTo(Readings_Buffer.readLine().toString());
                Log.i(TAG, "daysBefore: " + nextTimeSlot.getLocation());

                TimeslotList.add(nextTimeSlot);
            }

            Collections.sort(TimeslotList, new Comparator<TimeSlot>() {
                @Override
                public int compare(TimeSlot o1, TimeSlot o2) {

                    int indexOfColon = o1.getTimeFrom().indexOf(':',0);
                    int time1 = Integer.parseInt( o1.getTimeFrom().substring(0,indexOfColon));
                    if(time1 <8) time1+=12;

                    indexOfColon = o2.getTimeFrom().indexOf(':',0);
                    int time2 = Integer.parseInt( o2.getTimeFrom().substring(0,indexOfColon));
                    if(time2 <8) time2+=12;

                    return (time1<time2)? -1 : (time1>time2)? 1: 0;
                    }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void Invoke_Toast(String ToastMessage, Context ctx) {
        Toast Error_CreatingDeadline = Toast.makeText(ctx, ToastMessage, Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    public boolean isEmpty() {
        return this.TimeslotList.isEmpty();
    }
}
