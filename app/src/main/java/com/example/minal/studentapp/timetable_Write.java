package com.example.minal.studentapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ahmed on 4/15/2018.
 */

public class timetable_Write {

    //Data firelds:
    static final String All_Files_Names = "AllFiles2";
    private FileOutputStream NewTimetablesFile;
    private static final String TAG = "Deadline_write";

    //Methods
    public timetable_Write(Context ctx, String DayLabel)
    {

        try {
            NewTimetablesFile = ctx.openFileOutput(DayLabel, ctx.MODE_PRIVATE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void Invoke_Toast(String ToastMessage,Context ctx)
    {
        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    public void addTimeTo(String TimeTo)
    {
        TimeTo += "\n";

        try
        {
            this.NewTimetablesFile.write(TimeTo.getBytes());
            Log.i(TAG,"Label Written: "+ TimeTo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addTimeFrom(String TimeFrom)
    {
        if( TimeFrom == null || TimeFrom == "")
            TimeFrom = "00:00";

        Log.i(TAG,"Desc Written: "+ TimeFrom);

        try
        {
            this.NewTimetablesFile.write( TimeFrom.getBytes());
            this.NewTimetablesFile.write( "\n".getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addTitle(String Title)
    {

        try
        {
            Title += "\n";
            this.NewTimetablesFile.write( Title.getBytes());
            Log.i(TAG,"Type Written: "+ Title);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addType(String addType)
    {
        try
        {
            this.NewTimetablesFile.write( addType.getBytes());
            this.NewTimetablesFile.write( "\n".getBytes());

            Log.i(TAG,"Time Written: "+ addType + ", ");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addLocation(String Location)
    {
        try
        {
            this.NewTimetablesFile.write( Location.getBytes());
            this.NewTimetablesFile.write( "\n".getBytes());

            Log.i(TAG,"DueDate Written: "+ Location);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void CloseFile()
    {
        try
        {
            NewTimetablesFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
