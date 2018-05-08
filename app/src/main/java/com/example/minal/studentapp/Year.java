package com.example.minal.studentapp;

import java.util.List;

/**
 * Created by ahmed on 5/5/2018.
 */

public class Year {

    private  List<Semester_GradeTerm> YearsGrades; //every year has list of grades

    public List<Semester_GradeTerm> getYearsGrades() {
        return YearsGrades;
    }

    public Year(List<Semester_GradeTerm> thisYearsGrades)
    {
        YearsGrades =thisYearsGrades;
    }



}
