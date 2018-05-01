package com.example.minal.studentapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.example.minal.studentapp.edit_deadline.FileName;

/**
 * Created by ahmed on 4/24/2018.
 */

public class News_Site {

    private FileOutputStream NewsSiteFile;
    private Context cntx;
    private String FileName;
    public News_Site(Context ctx,String filenam){
        this.cntx = ctx;
        FileName=filenam;
    }

    public boolean saveData(String savedData)
    {
        try
        {
            NewsSiteFile = cntx.openFileOutput(FileName, this.cntx.MODE_PRIVATE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {

            this.NewsSiteFile.write(savedData.getBytes());
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String readSavedData()
    {
        FileInputStream ReadFile = null;
        try {
            ReadFile = cntx.openFileInput(FileName);
            InputStreamReader Reader = new InputStreamReader(ReadFile);
            BufferedReader Readings_Buffer = new BufferedReader(Reader);
            String newsString = "";
            String bufferedLine="";

            while ((bufferedLine = Readings_Buffer.readLine()) != null)
            {
                newsString += bufferedLine+"\n";
            }

            return newsString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}