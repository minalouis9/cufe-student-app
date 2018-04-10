package com.example.minal.studentapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by ahmed on 3/6/2018.
 */

public class ReadDeadlines {

    //Data fields:
    private List<Deadline> DeadlinesList;
    public static ReadDeadlines _instance = null;

    //Methods:
    public  static  ReadDeadlines get_instance(Context cntx, List<Deadline> deadlinesLiseIn)
    {
        if(_instance == null)
            _instance = new ReadDeadlines(cntx,deadlinesLiseIn);
        else
        {
            deadlinesLiseIn = _instance.getList();
        }

        return _instance;
    }

    public ReadDeadlines(Context cntx,List<Deadline> deadlinesLiseIn)
    {
        DeadlinesList = deadlinesLiseIn;
        LoadAllDeadlines(cntx);
    }

    private void LoadAllDeadlines(Context cntx)
    {
        try
        {
            String FileName;
            FileInputStream ReadFile = cntx.openFileInput("AllFiles2");
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            int i=0;

            while ((FileName = Readings_Buffer.readLine()) != null)
            {
                DeadlinesList.add( new Deadline(FileName, cntx,++i));
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            this.Invoke_Toast("file not found",cntx);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            this.Invoke_Toast("Could not read Reminder!\nTry changing the Label and try again",cntx);
        }
    }

    private void Invoke_Toast(String ToastMessage,Context ctx)
    {
        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    public String getNextLabel()
    {
        return this.DeadlinesList.get(0).GetBasicInfo();
    }

    public boolean isEmpty()
    {
        return this.DeadlinesList.isEmpty();
    }

    public List<Deadline> getList()
    {
        return  this.DeadlinesList;
    }
}
