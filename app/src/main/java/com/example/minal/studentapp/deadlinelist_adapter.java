package com.example.minal.studentapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ahmed on 3/7/2018.
 */

import java.util.List;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;



public class deadlinelist_adapter extends RecyclerView.Adapter<deadlinelist_adapter.MyViewHolder> {
    private Context context;
    private List<Deadline> cartList;
    private String ThisDeadlineLabel;
    Activity parent;

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


    public deadlinelist_adapter(Context context, List<Deadline> cartList, Activity inParent) {
        this.context = context;
        this.cartList = cartList;
        this.cartList.clear();
        this.parent = inParent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deadline_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Deadline deadline = cartList.get(position);
        holder.name.setText(deadline.getLabel());
        ThisDeadlineLabel = deadline.getLabel();

        holder.description.setText(deadline.getDescription());
        holder.type.setText(deadline.getType());
        holder.DueDate.setText("Due: " + deadline.getDueDate());
        holder.HoursAndDaysLeft.setText(deadline.getDifferenceBetweenDates() + " from now");
        holder.thumbnail.setImageResource(deadline.getThumbnail());

        holder.name.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );

        holder.description.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );
        holder.type.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );
        holder.DueDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );
        holder.HoursAndDaysLeft.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );
        holder.thumbnail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );



        CardView thisViewCardView = (CardView) this.parent.findViewById(R.id.deadlineCardView);

        thisViewCardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartDialogue(deadline);
                    }
                }
        );
        holder.thisView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                StartDialogue(deadline);

            }
        });

    }

    private void StartDialogue(final Deadline deadline)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.deadline_dialoguebox);
        dialog.setTitle(deadline.getLabel());

        // set the custom dialog components - text, image and button

        TextView text = (TextView) dialog.findViewById(R.id.Title);
        text.setText("Alarm starts after:");

        TextView text2 = (TextView) dialog.findViewById(R.id.timeStamp);
        text2.setText(deadline.getDifferenceBetweenDates() + " from now");

        TextView text3 = (TextView) dialog.findViewById(R.id.Type);
        text3.setText(deadline.getType());

        Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogButtonEdit = (Button) dialog.findViewById(R.id.dialogButtonEdit);
        // if button is clicked, close the custom dialog
        dialogButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDeadline(deadline.getLabel());
            }
        });

        Button dialogButtonDelete = (Button) dialog.findViewById(R.id.dialogButtonDelete);
        // if button is clicked, close the custom dialog
        dialogButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadDeadlines.DestroyDeadline(deadline.getLabel(),context);
                Intent To_Deadlines = new Intent(context, Deadlines_Track.class);
                context.startActivity(To_Deadlines);
            }
        });


        dialog.show();
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
