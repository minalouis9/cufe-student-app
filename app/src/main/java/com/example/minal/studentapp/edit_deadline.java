package com.example.minal.studentapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class edit_deadline extends AppCompatActivity {
    public static String FileName = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent IntentActivity2 = new Intent(this,Deadlines_Track.class);
        startActivity(IntentActivity2);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deadline);
            //first thing is to create the deadline itself
        Deadline currentDeadline_Object = new Deadline(FileName,getApplicationContext(),0);


        //Update this interface:
        updateInterface(currentDeadline_Object);

        TextView Edit_Button = (TextView) findViewById(R.id.textViewBtn);
        Edit_Button.setText("Edit");

        Edit_Button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String TM="";
                        try {
                            if(CheckNoEmptyField(TM))
                            {
                                EditDeadline();
                            }
                            else
                            {
                                Invoke_Toast(TM,edit_deadline.this);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Intent To_Deadlines = new Intent(getBaseContext(), Deadlines_Track.class);
                        startActivity(To_Deadlines);
                    }
                }
        );


        final TextView DueDateText_New = (TextView) findViewById(R.id.DueDateText_Edit);
        final DatePickerDialog.OnDateSetListener datepicker;

        datepicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dateString = dayOfMonth + "/" + month + "/" + year;

                DueDateText_New.setText("Due Date: " + dateString);
            }
        };

        DueDateText_New.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int Month = calendar.get(Calendar.MONTH);
                        int Day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                edit_deadline.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar, datepicker,
                                year, Month, Day);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(("#009696"))));
                        dialog.show();
                    }
                }
        );
    }

    private void EditDeadline()
    {
        // Instantiating varables for all existing Data:
        EditText DeadlineLabel_Text = (EditText) findViewById(R.id.Label_Text_Edit);
        MultiAutoCompleteTextView DeadlineDesc_Text  = (MultiAutoCompleteTextView) findViewById(R.id.Desc_Txt_Edit);
        RadioButton Radiobutton_Quiz = (RadioButton) findViewById(R.id.Quiz_RadioButton_Edit);
        RadioButton Radiobutton_Project = (RadioButton) findViewById(R.id.ProjectPhase_RadioButton_Edit);
        RadioButton Radiobutton_Ass  = (RadioButton) findViewById(R.id.Assignment_RadioButton_Edit);
        EditText DeadlineDaysToRemind_Text = (EditText) findViewById(R.id.ElapsedDays_Text_Edit);
        TextView DueDateText_New = (TextView)findViewById(R.id.DueDateText_Edit);



        ReadDeadlines.DestroyDeadline(FileName,getApplicationContext());
        //Reading Values:
        Deadline_Write newDeadlineFile = new Deadline_Write(DeadlineLabel_Text.getText().toString(), getApplicationContext());

        newDeadlineFile.addDeadlineLabel(DeadlineLabel_Text.getText().toString());
        newDeadlineFile.addDeadlineDescription(DeadlineDesc_Text.getText().toString());

        if( Radiobutton_Quiz.isChecked()) newDeadlineFile.addDeadlineType("Quiz");
        else if( Radiobutton_Project.isChecked()) newDeadlineFile.addDeadlineType("Project");
        else if( Radiobutton_Ass.isChecked()) newDeadlineFile.addDeadlineType("Assignment");
        else newDeadlineFile.addDeadlineType("Other");

        newDeadlineFile.addDeadlineDueDate(DueDateText_New.getText().toString().substring(10,DueDateText_New.getText().toString().length()));
        newDeadlineFile.addDeadlineTimeElapsed(DeadlineDaysToRemind_Text.getText().toString());
        newDeadlineFile.CloseFile();

    }


    private Boolean CheckNoEmptyField(String TM) throws ParseException {
        EditText DeadlineLabel_Text = (EditText) findViewById(R.id.Label_Text_Edit);
        MultiAutoCompleteTextView DeadlineDesc_Text  = (MultiAutoCompleteTextView) findViewById(R.id.Desc_Txt_Edit);
        RadioButton Radiobutton_Quiz = (RadioButton) findViewById(R.id.Quiz_RadioButton_Edit);
        RadioButton Radiobutton_Project = (RadioButton) findViewById(R.id.ProjectPhase_RadioButton_Edit);
        RadioButton Radiobutton_Ass  = (RadioButton) findViewById(R.id.Assignment_RadioButton_Edit);
        RadioButton Radiobutton_Other = (RadioButton) findViewById(R.id.Other_RadioButton_Edit);
        EditText DeadlineDaysToRemind_Text = (EditText) findViewById(R.id.ElapsedDays_Text_Edit);
        TextView DueDateText_New =(TextView) findViewById(R.id.DueDateText_Edit);


        if(DeadlineLabel_Text.getText().toString() == null)
        {
            TM = "Please Fill Label Field";
            return false;
        }

        if(DeadlineDesc_Text.getText().toString() == null)
        {
            TM = "Please Fill Description Field";
            return false;
        }

        if(Radiobutton_Quiz.isChecked() == false &&Radiobutton_Project.isChecked() == false && Radiobutton_Ass.isChecked() == false&&Radiobutton_Other.isChecked() == false)
        {
            TM = "Please Choose Deadline Type";
            return false;
        }

        if(DeadlineDaysToRemind_Text.getText().toString() == null)
        {
            TM = "Please Fill Description Field";
            return false;
        }

        String formatedDate = DueDateText_New.getText().toString().substring(10,DueDateText_New.getText().toString().length());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(formatedDate);

        if(date.compareTo(new Date()) <0)
        {
            TM = "Date Should Not be Earlier Than Today";
            return false;
        }

        if(DueDateText_New.getText().toString().compareTo("Select Due Date") == 0)
        {
            return false;
        }
        return true;
    }


    private void Invoke_Toast(String ToastMessage,Context ctx){

        Toast Error_CreatingDeadline = Toast.makeText(ctx,ToastMessage,Toast.LENGTH_LONG);
        Error_CreatingDeadline.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        Error_CreatingDeadline.show();
    }



    private void updateInterface(Deadline deadline)
    {
        try {
            UpdateHeader(deadline.getLabel());
            UpdateDescription(deadline.getDescription());
            UpdateDaysElapsed(deadline.getDaysBefor(),deadline.getDueDate());
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
        MultiAutoCompleteTextView Desc_textview = (MultiAutoCompleteTextView) findViewById(R.id.Desc_Txt_Edit);
        Desc_textview.setText(Desc);
    }

    private void UpdateDaysElapsed(String Days,String DueDate)
    {
        TextView Days_textview = (TextView) findViewById(R.id.ElapsedDays_Text_Edit);
        TextView DueDateText_Edit = (TextView) findViewById(R.id.DueDateText_Edit);
        DueDateText_Edit.setText("Due Date: "+ DueDate);
        Days_textview.setText(Days);
    }



    private void UpdateType(String type) {

        if (type.compareTo("Quiz") == 0) {
            RadioButton quiz_RadioButton = (RadioButton) findViewById(R.id.Quiz_RadioButton_Edit);
            quiz_RadioButton.setChecked(true);
        } else if (type.compareTo("Assignment") == 0)
        {
            RadioButton assignment_RadioButton = (RadioButton) findViewById(R.id.Assignment_RadioButton_Edit);
            assignment_RadioButton.setChecked(true);
        }
        else if(type.compareTo("Project")==0) {
            RadioButton project_RadioButton = (RadioButton) findViewById(R.id.ProjectPhase_RadioButton_Edit);
            project_RadioButton.setChecked(true);
        }
        else {
            RadioButton other_RadioButton = (RadioButton) findViewById(R.id.Other_RadioButton_Edit);
            other_RadioButton.setChecked(true);
        }
    }
}
