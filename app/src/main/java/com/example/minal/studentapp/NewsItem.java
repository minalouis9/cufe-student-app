package com.example.minal.studentapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PublicKey;

/**
 * Created by ahmed on 5/3/2018.
 */

public class NewsItem {

    private String NewsDesc;
    private FileOutputStream NewsFile;

    public NewsItem(String Title,Context ctx) {

        try {
            NewsFile = ctx.openFileOutput(Title, ctx.MODE_PRIVATE);
            RegisterFile(Title, ctx);
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NewsItem(int Type,Context cntx)
    {
        if(Type==0)
        {
            ReadNews_Credit(cntx);
        }
        else
        {
            ReadNews_Semester(cntx);
        }

    }

    private void ReadNews_Credit(Context cntx)
    {
      FileInputStream ReadFile = null;
        try {
            ReadFile = cntx.openFileInput("Credit_All_News_Items");
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            NewsDesc = Readings_Buffer.readLine().toString();

    } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReadNews_Semester(Context cntx)
    {
        FileInputStream ReadFile = null;
        try {
            ReadFile = cntx.openFileInput("Sem_All_News_Items");
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            NewsDesc = Readings_Buffer.readLine().toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getNewsDesc()
    {
        return this.NewsDesc;
    }

    private void RegisterFile(String FileName,Context cntx)  {
        //This method is invoked only if the file for deadline is available name-wise:
        // Hence, we add the name directly

        try {
            FileOutputStream NewsTitlesRegisterFile = null;
            if(LoginActivity.username.charAt(0)=='1') {
                NewsTitlesRegisterFile = cntx.openFileOutput("Credit_All_News_Items", Context.MODE_APPEND);

            }
            else
            {
                NewsTitlesRegisterFile = cntx.openFileOutput("Sem_All_News_Items", Context.MODE_APPEND);
            }
            NewsTitlesRegisterFile.write(FileName.getBytes());
            NewsTitlesRegisterFile.write("\n".getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
