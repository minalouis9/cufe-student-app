package com.example.minal.studentapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed on 4/13/2018.
 */

public class CustomViewPage_Adapter extends PagerAdapter {


    private List<TimeSlot> cartList;
    private List<Model> modelsList;
    private Context cntxt;
    private String[] DaysInitials = {"Sat","Sun","Mon","Tues","Wed","Thurs"};
    private String[] Days = { LoginActivity.username +"_"+"Saturday",LoginActivity.username +"_"+"Sunday",LoginActivity.username +"_"+"Monday",LoginActivity.username +"_"+"Tuesday",LoginActivity.username +"_"+"Wednesday",LoginActivity.username +"_"+"Thursday"};


    public CustomViewPage_Adapter(Context inCont ,List<TimeSlot> inCartList)
    {
        this.modelsList = new ArrayList<>();
        initializeModels();
        this.cartList =inCartList;
        this.cntxt = inCont;
    }

    private void initializeModels(){
        for(int i=0;i<6;++i)
        {
            modelsList.add(new Model(DaysInitials[i],0));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return DaysInitials[position];
    }

    @Override
    public int getCount() {
        return modelsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewGroup)container).removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){

        LayoutInflater inflater = LayoutInflater.from(this.cntxt);
        View itemViewer = inflater.inflate(R.layout.timetable_list_item,container,false);

        fetchRecipes( position);

        container.addView(itemViewer);
        return itemViewer;
        //return null;
    }

    private void fetchRecipes( int i)
    {
        try
        {
            // Shoud be replaced by :
            if( this.cartList.isEmpty() == false ) this.cartList.clear();
            Read_Timetable_Offline offline_TimeTable = new Read_Timetable_Offline(cntxt, this.cartList, Days[i]);
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

}
