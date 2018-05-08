package com.example.minal.studentapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.RequiresPermission;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ahmed on 3/6/2018.
 */

public class ReadDeadlines {
    public static List<Deadline> DeadlinesList = new ArrayList<>();
    private Context context;
    static private List<String> deadlinesNames = new ArrayList<String>();
    private SharedPreferences.Editor loginPrefs_Editor=null;


    public ReadDeadlines(Context cntx,List<Deadline> deadlinesLiseIn)
    {
        this.context = cntx;
        DeadlinesList = deadlinesLiseIn;
        LoadAllDeadlines(cntx,LoginActivity.username);

    }

    public static void LoadAllDeadlines(Context cntx,String ID)
    {
        deadlinesNames.clear();
        DeadlinesList.clear();
        try {
            String FileName;

            if(ID==null||ID.length()==0) //No Data Read
            {//Try using Parser:
                    return;
            }

            FileInputStream ReadFile = cntx.openFileInput( ID +"_"+ "AllFiles");

            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            int i=0;
            while ((FileName = Readings_Buffer.readLine()) != null)
            {
                deadlinesNames.add(FileName);
                DeadlinesList.add( new Deadline(FileName, cntx,++i));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //this.Invoke_Toast("file not found",cntx);
        } catch (IOException e) {
            e.printStackTrace();
            //this.Invoke_Toast("Could not read Reminder!\nTry changing the Label and try again",cntx);
        }

        //Now Sort The Data:
        Collections.sort(DeadlinesList, new Comparator<Deadline>() {
            @Override
            public int compare(Deadline o1, Deadline o2) {



                SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
                Date firstDate = null;
                Date secondDate = null;
                try {
                    firstDate = sdf.parse(o1.getDueDate());
                    secondDate = sdf.parse(o2.getDueDate());


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return (firstDate.compareTo(secondDate) < 0)? -1 :((firstDate.compareTo(secondDate) == 0)? 0:1) ;
            }

        });


        if(DeadlinesList.size()>0) {
            SharedPreferences sharedpreferences = cntx.getSharedPreferences("Deadlines", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("Deadlines_Notification_Message", DeadlinesList.get(0).getLabel());
            editor.putString("Deadlines_DueDate", DeadlinesList.get(0).getDueDate());
            editor.commit();
        }

    }




    public static String[] getNoyification(Context cntx) {

        ReadDeadlines.LoadAllDeadlines(cntx, LoginActivity.username);

        if (DeadlinesList.size() > 0) {

            //subtract the time befor, and put firing day
            String DueDate = DeadlinesList.get(0).getDueDate();
            int daysBefore = Integer.parseInt(DeadlinesList.get(0).getDaysBefor()) * -1;

            Calendar calendar_5DaysEarlierThanToday = Calendar.getInstance(); // this would default to now
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                calendar_5DaysEarlierThanToday.setTime(sdf.parse(DeadlinesList.get(0).getDueDate()));
                calendar_5DaysEarlierThanToday.add(Calendar.DAY_OF_MONTH, daysBefore);
                DueDate = sdf.format(calendar_5DaysEarlierThanToday.getTime());
                return new String[]{DeadlinesList.get(0).getLabel(),DueDate,(daysBefore*-1)+""};
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            return new String[]{"No Deadlines Yet","1/1/2000","1"};
        }

        return new String[]{"No Deadlines Yet","1/1/2000","1"};
    }

    static public void DestroyDeadline(String DeadlineLabelToDestroy, Context context)
    {
        if(deadlinesNames.isEmpty()) return;

        deadlinesNames.remove(deadlinesNames.indexOf(DeadlineLabelToDestroy)); //Remove this deadline

        //we should delete this file here

        //Now remove it also from the AllFiles File:
        FileOutputStream DeadlinesNamesRegisterFile = null;
        try {
            DeadlinesNamesRegisterFile = context.openFileOutput(Deadline_Write.All_Files_Names, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<deadlinesNames.size();++i) {

            try {
                DeadlinesNamesRegisterFile.write(deadlinesNames.get(i).getBytes());
                DeadlinesNamesRegisterFile.write("\n".getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        deadlinesNames.clear();

    }

    private void Invoke_Toast(String ToastMessage,Context ctx){

        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    public boolean isEmpty()
    {
        return this.DeadlinesList.isEmpty();
    }

}
