package com.example.minal.studentapp;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Deadline_Write {

    //Data firelds:
    static final String All_Files_Names = "AllFiles2";
    private FileOutputStream NewDeadlineFile;
    private static final String TAG = "Deadline_write";

    //Methods:
    private void RegisterFile(String FileName,Context cntx) throws IOException {

        //This method is invoked only if the file for deadline is available name-wise:
        // Hence, we add the name directly

        FileOutputStream DeadlinesNamesRegisterFile = cntx.openFileOutput(Deadline_Write.All_Files_Names,Context.MODE_APPEND);
        DeadlinesNamesRegisterFile.write(FileName.getBytes());
        DeadlinesNamesRegisterFile.write("\n".getBytes());
    }

    public Deadline_Write(String DeadlineFileNameInput,Context ctx)
    {

        try
        {
            NewDeadlineFile = ctx.openFileOutput(DeadlineFileNameInput, ctx.MODE_PRIVATE);
            RegisterFile(DeadlineFileNameInput,ctx);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String ReadLabel(String FileName,Context ctx)
    {
        try
        {
            FileInputStream ReadFile = ctx.openFileInput(FileName);

            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);

            return Readings_Buffer.readLine() + "\n";
        }
        catch (FileNotFoundException e)
        {
            this.Invoke_Toast("Could not create Reminder!\nTry changing the Label and try again",ctx);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "Could Not Read Data";
    }

    private void Invoke_Toast(String ToastMessage,Context ctx)
    {
        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    public Deadline_Write(Context ctx)
    {
        try
        {
            NewDeadlineFile = ctx.openFileOutput("New_Deadline", ctx.MODE_PRIVATE);
            RegisterFile("New_Deadline",ctx);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            Toast Erro_CreatingDeadline = Toast.makeText(ctx,"Could not create Reminder!\nTry changing the Label and try again",Toast.LENGTH_LONG);
            Erro_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            Erro_CreatingDeadline.show();
        }
    }

    public void addDeadlineLabel(String Label)
    {
        Label += "\n";

        try
        {
            this.NewDeadlineFile.write(Label.getBytes());
            Log.i(TAG,"Label Written: "+ Label);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addDeadlineDescription(String Description)
    {
        if( Description == null || Description == "")
            Description = "NO_Description";

        Log.i(TAG,"Desc Written: "+ Description);

        try
        {
            this.NewDeadlineFile.write( Description.getBytes());
            this.NewDeadlineFile.write( "\n".getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addDeadlineType(String Type)
    {

        try
        {
            Type += "\n";
            this.NewDeadlineFile.write( Type.getBytes());
            Log.i(TAG,"Type Written: "+ Type);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addDeadlineTimeElapsed(String Days)
    {
        try
        {
            this.NewDeadlineFile.write( Days.getBytes());
            this.NewDeadlineFile.write( "\n".getBytes());

            Log.i(TAG,"Time Written: "+ Days + ", ");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addDeadlineDueDate(String Date)
    {
        try
        {
            this.NewDeadlineFile.write( Date.getBytes());
            this.NewDeadlineFile.write( "\n".getBytes());

            Log.i(TAG,"DueDate Written: "+ Date);
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
            NewDeadlineFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
