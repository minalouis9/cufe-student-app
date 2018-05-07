package com.example.minal.studentapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ahmed on 4/15/2018.
 */


public class timetable_Adapter extends RecyclerView.Adapter<timetable_Adapter.MyViewHolder> {

    //Data firelds:
    private Context context;
    private List<TimeSlot> cartList;

    //Methods:

    //internal class MyViewHolder declaration:
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView timeFrom,timeTo,Location;
        public TextView Title, Type;
        View thisView;

        public MyViewHolder(View view)
        {
            super(view);

            timeFrom = view.findViewById(R.id.TimeFrom);
            Title = view.findViewById(R.id.Title);
            Type = view.findViewById(R.id.Type);
            Location = view.findViewById(R.id.Location);
            this.thisView = view;
        }
    }

    public timetable_Adapter(Context context, List<TimeSlot> cartList)
    {
        this.context = context;
        this.cartList = cartList;
        this.cartList.clear();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final TimeSlot slot = cartList.get(position);

        holder.timeFrom.setText( slot.getTimeFrom() + " - " +slot.getTimeTo());
     //   holder.timeTo.setText(slot.getTimeTo());
        holder.Title.setText(slot.getTitle());
        holder.Type.setText(slot.getType());
        holder.Location.setText(slot.getLocation());

        holder.thisView.setBackground(Drawable.createFromPath("@drawable/listbkgrnd.png"));
    }

    @Override
    public int getItemCount()
    {
        return cartList.size();
    }

    private void OpenDeadline(String filename)
    {
        Intent To_EditDeadlines = new Intent(context, edit_deadline.class);
        edit_deadline.FileName = filename;
        context.startActivity(To_EditDeadlines);
    }
}
