package com.example.minal.studentapp;

/**
 * Created by lenovoo on 02/05/2018.
 */

public class Attendance_info {
   private String Course_name,Day,Week,Session;
   Attendance_info(String name, String day, String week, String session)
   {
       Course_name=name;
       Day=day;
       Week=week;
       Session=session;
   }
   public String getCourse_name()
   {return Course_name;}
    public String getCourse_week()
    {return Week;}
    public String getCourse_day()
    {return Day;}
    public String getCourse_session()
    {return Session;}
 }
