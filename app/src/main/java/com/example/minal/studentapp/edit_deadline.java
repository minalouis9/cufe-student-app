package com.example.minal.studentapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class edit_deadline extends AppCompatActivity {

    //Data firelds:
    public static String FileName = null;

    //Methods:
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deadline);

        //first thing is to create the deadline itself
        Deadline currentDeadline_Object = new Deadline(FileName,getApplicationContext(),0);
        //Update this interface:
        updateInterface(currentDeadline_Object);

        TextView Edit_Button = (TextView) findViewById(R.id.textViewBtn);
        Edit_Button.setText("Edit");
    }

    private void Invoke_Toast(String ToastMessage,Context ctx)
    {
        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }

    private void updateInterface(Deadline deadline)
    {
        try
        {
            UpdateHeader(deadline.getLabel());
            UpdateDescription(deadline.getDescription());
            UpdateDaysElapsed(deadline.getDaysBefor());
            UpdateType(deadline.getType());
        }
        catch(Exception e)
        {
            Invoke_Toast("Couldnot read this deadline",getApplicationContext());
        }
    }

    private void UpdateHeader(String label)
    {
        TextView header_textview = (TextView) findViewById(R.id.Header_Text_Edit);
        header_textview.setText(label);
        header_textview = (TextView) findViewById(R.id.Label_Text_Edit);
        header_textview.setText(label);
    }

    private void UpdateDescription(String Desc)
    {
        TextView Desc_textview = (TextView) findViewById(R.id.Desc_Txt_Edit);
        Desc_textview.setText(Desc);
    }

    private void UpdateDaysElapsed(String Days)
    {
        TextView Days_textview = (TextView) findViewById(R.id.ElapsedDays_Text_Edit);
        Days_textview.setText(Days);
    }

    private void UpdateType(String type)
    {
        if (type.compareTo("Quiz") == 0)
        {
            RadioButton quiz_RadioButton = (RadioButton) findViewById(R.id.Quiz_RadioButton_Edit);
            quiz_RadioButton.setChecked(true);
        }
        else if (type.compareTo("Assignment") == 0)
        {
            RadioButton assignment_RadioButton = (RadioButton) findViewById(R.id.Assignment_RadioButton_Edit);
            assignment_RadioButton.setChecked(true);
        }
        else if(type.compareTo("Project")==0)
        {
            RadioButton project_RadioButton = (RadioButton) findViewById(R.id.ProjectPhase_RadioButton_Edit);
            project_RadioButton.setChecked(true);
        }
        else
        {
            RadioButton other_RadioButton = (RadioButton) findViewById(R.id.Other_RadioButton_Edit);
            other_RadioButton.setChecked(true);
        }
    }
}
