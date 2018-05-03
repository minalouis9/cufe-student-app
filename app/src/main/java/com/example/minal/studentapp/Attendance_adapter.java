package com.example.minal.studentapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ahmed on 3/7/2018.
 */

import java.util.List;
public class Attendance_adapter extends RecyclerView.Adapter<Attendance_adapter.MyViewHolder> {
    private Context context;
    private List<Attendance_info> cartList;


    //internal class MyViewHolder declaration:
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Subject_name, Day, Week, Session;
        View view;

        public MyViewHolder(View view) {
            super(view);
            Subject_name = view.findViewById(R.id.SubjectName1);
            Day = view.findViewById(R.id.Day1);
            Week = view.findViewById(R.id.Week1);
            Session = view.findViewById(R.id.Session1);
            this.view=view;
            view.setClickable(false);

        }
    }


    public  Attendance_adapter(Context context, List<Attendance_info> cartList) {
        this.context = context;

        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Attendance_info att = cartList.get(position);
        holder.Subject_name.setText(att.getCourse_name());
        // holder.description.setText( deadline.getDescription());
        holder.Week.setText(att.getCourse_week());
        holder.Day.setText(att.getCourse_day());
        //holder.DueDate.setText("Due: "+ deadline.UpdateDaysAndHoursBefore(TimeUnit.DAYS));
        holder.Session.setText( att.getCourse_session());
        //holder.HoursAndDaysLeft.setText(deadline.getHoursBefore()+" Hours,and "+deadline.getDaysBefor()+" Days");

        }

    // recipe
    @Override
    public int getItemCount() {
        return cartList.size();
    }


}
