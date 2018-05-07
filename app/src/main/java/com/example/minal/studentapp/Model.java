package com.example.minal.studentapp;

/**
 * Created by ahmed on 4/13/2018.
 */

public class Model {
    private  String title;
    private  int id;

    public Model(){}
    public Model(String inTitle, int inID){
        this.title = inTitle;
        this.id = inID;
    }

    public void setID(int inID){this.id = inID;}
    public int getID(){return this.id;}

    public void setTitle(String inTitle){this.title = inTitle;}
    public String getTitle(){return this.title;}
}
