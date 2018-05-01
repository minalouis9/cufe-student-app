package com.example.minal.studentapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewDeadline extends AppCompatActivity {



    public class CustomDatePicker extends DatePicker
    {
        public CustomDatePicker(Context context, AttributeSet attrs, int
                defStyle)
        {
            super(context, attrs, defStyle);
        }

        public CustomDatePicker(Context context, AttributeSet attrs)
        {
            super(context, attrs);
        }

        public CustomDatePicker(Context context)
        {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev)
        {
        /* Prevent parent controls from stealing our events once we've
gotten a touch down */
            if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
            {
                ViewParent p = getParent();
                if (p != null)
                    p.requestDisallowInterceptTouchEvent(true);
            }

            return false;
        }
    }

    private static final String TAG = "Deadline Tags";
    public static  String Mode = "ND";

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent IntentActivity2 = new Intent(this,Deadlines_Track.class);
        startActivity(IntentActivity2);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_deadline);
        Log.i(TAG, "NewDeadline OnCreate is invoked");


        final TextView DueDateText_New = (TextView) findViewById(R.id.DueDateText_New);
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
                                NewDeadline.this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar, datepicker,
                                year, Month, Day);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(("#009696"))));
                        dialog.show();
                    }
                }
        );


        CardView NewDeadline_Invoke = findViewById(R.id.Add_Card);

        final String ToastMessage = "Not Yet";

        NewDeadline_Invoke.setOnClickListener(
                new CardView.OnClickListener() {
                    public void onClick(View v) {
                        Log.i(TAG, "Listener is invoked----------------------------------------------");

                        try {
                            if (CheckNoEmptyField(ToastMessage) == false) {

                                Invoke_Toast(ToastMessage, getApplicationContext());
                            } else {
                                if (Mode.compareTo("ND") == 0) CreateDeadline();
                                else {

                                }
                                //Shift Back to Deadlines Viewer:
                                Intent To_Deadlines = new Intent(getBaseContext(), Deadlines_Track.class);
                                startActivity(To_Deadlines);
                            }
                        } catch (ParseException e) {
                        }
                    }
                }

        );

    }



    public void CreateDeadline()
    {
        // Instantiating varables for all existing Data:
        EditText DeadlineLabel_Text = (EditText) findViewById(R.id.Label_Text);
        MultiAutoCompleteTextView DeadlineDesc_Text  = (MultiAutoCompleteTextView) findViewById(R.id.Desc_Txt);
        RadioButton Radiobutton_Quiz = (RadioButton) findViewById(R.id.Quiz_RadioButton);
        RadioButton Radiobutton_Project = (RadioButton) findViewById(R.id.ProjectPhase_RadioButton);
        RadioButton Radiobutton_Ass  = (RadioButton) findViewById(R.id.Assignment_RadioButton);
        TextView DueDateText_New = (TextView)findViewById(R.id.DueDateText_New);
        EditText DeadlineDaysToRemind_Text = (EditText) findViewById(R.id.ElapsedDays_Text);

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

    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }


    //Utility functions:
    private Boolean CheckNoEmptyField(String TM) throws ParseException {
        EditText DeadlineLabel_Text = (EditText) findViewById(R.id.Label_Text);
        MultiAutoCompleteTextView DeadlineDesc_Text  = (MultiAutoCompleteTextView) findViewById(R.id.Desc_Txt);
        RadioButton Radiobutton_Quiz = (RadioButton) findViewById(R.id.Quiz_RadioButton);
        RadioButton Radiobutton_Project = (RadioButton) findViewById(R.id.ProjectPhase_RadioButton);
        RadioButton Radiobutton_Ass  = (RadioButton) findViewById(R.id.Assignment_RadioButton);
        RadioButton Radiobutton_Other = (RadioButton) findViewById(R.id.Other_RadioButton);
        EditText DeadlineDaysToRemind_Text = (EditText) findViewById(R.id.ElapsedDays_Text);
        TextView DueDateText_New =(TextView) findViewById(R.id.DueDateText_New);


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
}




