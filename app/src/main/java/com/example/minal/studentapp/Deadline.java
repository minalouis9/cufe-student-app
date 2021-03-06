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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ahmed on 3/6/2018.
 */

public class Deadline
{

    private String Label, Description, Type, DueDate, DaysBefor, HoursBefore;

    private static final String TAG = "Deadline_Instantiation";

    private int index;

    Deadline(String FileName, Context cntx, int inputIndex)
    {

        FileInputStream ReadFile = null;
        try {
            ReadFile = cntx.openFileInput(FileName);
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);

            this.setLabel(Readings_Buffer.readLine().toString());
            Log.i(TAG,"Label: "+ this.getLabel());
            this.setDescription(Readings_Buffer.readLine().toString());
            Log.i(TAG,"Desc: "+ this.getDescription());
            this.setType(Readings_Buffer.readLine().toString());
            Log.i(TAG,"Type: "+ this.getType());
            //this.setDueDate(Readings_Buffer.readLine().toString());
            this.setDueDate(Readings_Buffer.readLine().toString());
            Log.i(TAG,"Duedate: "+ this.getDueDate());
            this.setDaysBefor(Readings_Buffer.readLine().toString());
            Log.i(TAG,"daysBefore: "+ this.getDaysBefor());
            /*this.setHoursBefore(Readings_Buffer.readLine().toString());
            Log.i(TAG,"Hours before: "+ this.getHoursBefore());
*/
            this.index = inputIndex;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            this.Invoke_Toast("Could not Create Deadline! File Not Found",cntx);
        } catch (IOException e) {
            e.printStackTrace();
            this.Invoke_Toast("Could not create Reminder! File Could not be read",cntx);
        }


    }

    private void Invoke_Toast(String ToastMessage,Context ctx){

        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    public void setLabel(String LabelInput)
    {
        this.Label = LabelInput;
    }
    public String getLabel()
    {
        return this.Label;
    }

    public void setDescription(String DescriptionInput)
    {
        this.Description = DescriptionInput;
    }
    public String getDescription()
    {
        return this.Description;
    }

    public void setType(String TypeInput)
    {
        this.Type = TypeInput;
    }

    public String getType()
    {
        return this.Type;
    }

    public void setDueDate(String DueDateInput)
    {
        this.DueDate = DueDateInput;
    }
    public String getDueDate()
    {
        return this.DueDate;
    }

    public void setDaysBefor(String DaysBeforInput)
    {
        this.DaysBefor = DaysBeforInput;
    }
    public String getDaysBefor()
    {
        return this.DaysBefor;
    }

    public long UpdateDaysAndHoursBefore(TimeUnit timeUnit)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        Date firstDate = null;
        try {
            firstDate = sdf.parse(this.getDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date secondDate = null;
        try {
            //secondDate = sdf.parse((DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now())).toString());
            secondDate = sdf.parse(
                    Calendar.getInstance().getTime().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

   return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);

    }

    public int getThumbnail()
    {
        Log.i(TAG," Getting Thumbnail: " +"Type: " + this.getType()+" Label: "+this.getLabel());
        if(this.Type.equals("Quiz"))
        {
            Log.i(TAG,"Returning Quiz");
            return R.drawable.quiz;

        }
        else if(this.Type.equals("Assignment"))
        {
            Log.i(TAG,"Returning Assignment");
            return R.drawable.assignment;
        }
        else if(this.Type.equals("Project"))
        {
            Log.i(TAG,"Returning Project");
            return R.drawable.projectmanagemant;
        }
        else
        {
            Log.i(TAG,"Returning Other");
            return R.drawable.other;
        }
    }

    public String getDifferenceBetweenDates()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();



        // String str_date = "20/2/2018";
        DateFormat formatter;
        Date date2 = null;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date2 = formatter.parse(this.getDueDate());
            String dateString = dateFormat.format(date).substring(0,10);

            Map<TimeUnit,Long> map = computeDiff(date,date2);
            return getTimeElapsed(map);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Not_Calculated";
    }

    private String getTimeElapsed(Map<TimeUnit,Long> map)
    {
        String timeElapsed ="";
        boolean flag1=false,flag2=false,flag3=false;
        if(map.get(TimeUnit.DAYS) != 0)
        {
            timeElapsed+= map.get(TimeUnit.DAYS).toString() + " Days";
            flag1=true;
        }
        if(map.get(TimeUnit.HOURS) != 0)
        {
            if(flag1 == true) timeElapsed +=", ";
            timeElapsed+= map.get(TimeUnit.HOURS).toString() + " Hours";
            flag2 = true;
        }
        if(map.get(TimeUnit.MINUTES) != 0)
        {
            if(flag2 == true || flag1 == true) timeElapsed +=", ";
            timeElapsed+= map.get(TimeUnit.MINUTES).toString() + " Minutes";
            flag2 = true;
        }

       if(isOverdue(map)) timeElapsed = "Overdue by: " + timeElapsed;

        return timeElapsed;
    }

    public boolean fireAlarm() {
        int daysBefore = Integer.parseInt(this.getDaysBefor()) * -1;

        Calendar calendar_5DaysEarlierThanToday = Calendar.getInstance(); // this would default to now
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            calendar_5DaysEarlierThanToday.setTime(sdf.parse(this.getDueDate()));
            calendar_5DaysEarlierThanToday.add(Calendar.DAY_OF_MONTH, daysBefore);
            String formatedDate = sdf.format(calendar_5DaysEarlierThanToday.getTime());
            Date date5DaysEarlier =  sdf.parse(formatedDate);

            if(date5DaysEarlier.compareTo(new Date()) <= 0)
            {
                return true;
            }
            else return false;
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
return false;
    }

    private boolean isOverdue(Map<TimeUnit,Long> map)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date date = null;
        try {
            date = sdf.parse(this.getDueDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date.compareTo(new Date()) <0)
        {
            return true;
        }
        return false;
    }

    private Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        return result;
    }
}
