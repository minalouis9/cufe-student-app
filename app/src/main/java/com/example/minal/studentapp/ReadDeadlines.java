package com.example.minal.studentapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 3/6/2018.
 */

public class ReadDeadlines {
    private List<Deadline> DeadlinesList;
    private Context context;
    static private List<String> deadlinesNames = new ArrayList<String>();

    public ReadDeadlines(Context cntx,List<Deadline> deadlinesLiseIn)
    {
        this.context = cntx;
        DeadlinesList = deadlinesLiseIn;
        LoadAllDeadlines(cntx);

    }

    private void LoadAllDeadlines(Context cntx)
    {
        DeadlinesList.clear();
        deadlinesNames.clear();
        try {
            String FileName;
            FileInputStream ReadFile = cntx.openFileInput( LoginActivity.password +"_"+ "AllFiles");

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
    }

    static public void DestroyDeadline(String DeadlineLabelToDestroy, Context context)
    {
        if(deadlinesNames.isEmpty()) return;

        deadlinesNames.remove(deadlinesNames.indexOf(DeadlineLabelToDestroy)); //Remove this deadline

        //we should delete this file here

        //Now remove it also from the AllFiles File:
        FileOutputStream DeadlinesNamesRegisterFile = null;
        try {
            DeadlinesNamesRegisterFile = context.openFileOutput(Deadline_Write.All_Files_Names, Context.MODE_PRIVATE);
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
