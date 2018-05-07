package com.example.minal.studentapp;

/**
 * Created by ahmed on 5/4/2018.
 */

public class Semester_GradeTerm {

    private String CourseName;
    private String Term1_Grade;
    private String Term2_Grade;
    private String Term1_Grade_Max;
    private String TotalGrade;
    private String TotalGradeMax;

    public String getTotalGrade() {
        return TotalGrade;
    }

    public void setTotalGrade(String totalGrade) {
        TotalGrade = totalGrade;
    }

    public String getTotalGradeMax() {
        return TotalGradeMax;
    }

    public void setTotalGradeMax(String totalGradeMax) {
        TotalGradeMax = totalGradeMax;
    }


    public Semester_GradeTerm() {

    }

    public String getCourseName() {
        return CourseName;
    }

    public String getTerm1_Grade() {
        return Term1_Grade;
    }

    public String getTerm2_Grade() {
        return Term2_Grade;
    }

    public String getTerm1_Grade_Max() {
        return Term1_Grade_Max;
    }

    public String getTerm2_Grade_Max() {
        return Term2_Grade_Max;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public void setTerm1_Grade(String term1_Grade) {
        Term1_Grade = term1_Grade;
    }

    public void setTerm2_Grade(String term2_Grade) {
        Term2_Grade = term2_Grade;
    }

    public void setTerm1_Grade_Max(String term1_Grade_Max) {
        Term1_Grade_Max = term1_Grade_Max;
    }

    public void setTerm2_Grade_Max(String term2_Grade_Max) {
        Term2_Grade_Max = term2_Grade_Max;
    }

    private String Term2_Grade_Max;

}
