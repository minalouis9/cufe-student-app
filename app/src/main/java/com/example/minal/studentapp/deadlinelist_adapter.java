package com.example.minal.studentapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ahmed on 3/7/2018.
 */

import java.util.List;
public class deadlinelist_adapter extends RecyclerView.Adapter<deadlinelist_adapter.MyViewHolder> {
    private Context context;
    private List<Deadline> cartList;


    //internal class MyViewHolder declaration:
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, type, DueDate, HoursAndDaysLeft;
        public ImageView thumbnail;
        private View thisView;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.Description);
            type = view.findViewById(R.id.type);
            DueDate = view.findViewById(R.id.DueDate);
            HoursAndDaysLeft = view.findViewById(R.id.HoursAndDaysLeft);
            thumbnail = view.findViewById(R.id.thumbnail);

            thisView = view;
        }
    }


    public deadlinelist_adapter(Context context, List<Deadline> cartList) {
        this.context = context;

        this.cartList = cartList;
        this.cartList.clear();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deadline_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Deadline deadline = cartList.get(position);
        holder.name.setText(deadline.getLabel());
        // holder.description.setText( deadline.getDescription());
        holder.description.setText(" ");
        holder.type.setText(deadline.getType());
        //holder.DueDate.setText("Due: "+ deadline.UpdateDaysAndHoursBefore(TimeUnit.DAYS));
        holder.DueDate.setText("Due: " + deadline.getDueDate());
        //holder.HoursAndDaysLeft.setText(deadline.getHoursBefore()+" Hours,and "+deadline.getDaysBefor()+" Days");
        holder.HoursAndDaysLeft.setText(deadline.getDifferenceBetweenDates() + " from now");
        holder.thumbnail.setImageResource(deadline.getThumbnail());
        //holder.thumbnail.setBackground(Drawable.createFromPath(deadline.getThumbnail()));

        holder.thisView.setBackground(Drawable.createFromPath("drawable/backgroundforlist.png"));
        holder.thisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDeadline(deadline.getLabel());
            }
        });
        holder.thisView.setBackground(Drawable.createFromPath("B:\\lectures and assignements\\Spring 2018\\Software Engineering\\cufe-student-app-master\\app\\src\\main\\res\\drawable\\bkgrnd.png"));
    }
    // recipe
    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private void OpenDeadline(String filename)
    {
        Intent To_EditDeadlines = new Intent(context, edit_deadline.class);
        edit_deadline.FileName = filename;
        context.startActivity(To_EditDeadlines);


    }
}
